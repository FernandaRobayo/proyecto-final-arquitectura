# ADR-010 — Migración a Flyway para gestión versionada del esquema de base de datos

## Estado

Propuesto — 2026-05-02

---

# Contexto

Actualmente el esquema de base de datos se gestiona mediante `import.sql` ejecutado por Hibernate al arrancar el contenedor, con `SPRING_JPA_HIBERNATE_DDL_AUTO` parametrizado por variable de entorno.

Este mecanismo es adecuado para entornos de desarrollo limpio, pero no provee trazabilidad de cambios, capacidad de rollback ni control sobre la secuencia de evolución del esquema en entornos de larga vida.

---

# Problema

Con `import.sql` como único mecanismo de gestión de esquema:

* no existe un historial versionado de los cambios aplicados al esquema
* no es posible aplicar de forma incremental solo los cambios pendientes en un entorno existente
* un error en el esquema no tiene un mecanismo formal de rollback
* en producción con `ddl-auto=none`, cualquier cambio de esquema requiere intervención manual sin trazabilidad
* los desarrolladores no tienen visibilidad de qué migraciones se han aplicado en cada entorno

---

# Decisión Arquitectónica

Se propone adoptar **Flyway** como herramienta de gestión versionada del esquema de base de datos:

```
Herramienta   →  Flyway (spring-boot-starter-flyway)
Scripts       →  src/main/resources/db/migration/
                 V{version}__{descripcion}.sql
                 Ejemplo:
                   V1__create_initial_schema.sql
                   V2__auth_seed.sql
                   V3__add_audit_fields.sql

Control DDL   →  SPRING_JPA_HIBERNATE_DDL_AUTO=validate
                 Flyway gestiona DDL · Hibernate solo valida

Tabla control →  flyway_schema_history (gestionada automáticamente)
```

Los scripts `import.sql` y `auth-seed.sql` actuales se convierten en migraciones Flyway versionadas.

---

# Diagrama del flujo de migración

```
  Arranque del contenedor backend
                │
                ▼
┌───────────────────────────────────────────────┐
│              Flyway (auto-run)                │
│                                               │
│  Lee flyway_schema_history                    │
│  Compara con scripts en db/migration/         │
│  Aplica solo las migraciones pendientes       │
└───────────────────────┬───────────────────────┘
                        │
          ┌─────────────▼──────────────────┐
          │     db/migration/              │
          │                                │
          │  V1__initial_schema.sql  ✓     │
          │  V2__auth_seed.sql       ✓     │
          │  V3__add_audit_fields.sql ← nueva │
          └─────────────┬──────────────────┘
                        │
                        ▼
          ┌─────────────────────────────────┐
          │         MySQL 8.0               │
          │  Esquema actualizado            │
          │  flyway_schema_history poblado  │
          └─────────────────────────────────┘
```

---

# Alternativas consideradas

| Alternativa | Descripción | Por qué no se eligió |
|---|---|---|
| Mantener `import.sql` actual | Sin cambios en el mecanismo de bootstrap | No escala a entornos de larga vida; sin trazabilidad ni evolución incremental del esquema |
| Liquibase | Migraciones con formato XML / YAML / SQL | Más expresiva pero más compleja; Flyway con SQL puro es más accesible para el equipo actual |
| Migraciones manuales sin herramienta | Scripts aplicados manualmente por el DBA | No automatizable; no integrable con el arranque del contenedor; sin historial en el repositorio |

---

# Beneficios Arquitectónicos

* Cada cambio de esquema queda versionado y trazable en `flyway_schema_history`
* Las migraciones se aplican automáticamente al arrancar el contenedor, sin intervención manual
* `ddl-auto=validate` garantiza que Hibernate verifica la consistencia del esquema sin modificarlo
* Los entornos de CI/CD pueden resetear y migrar el esquema de forma reproducible
* El historial de migraciones es parte del repositorio y revisable en cualquier momento

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Historial versionado de cambios de esquema | Requiere convertir `import.sql` y `auth-seed.sql` a formato Flyway como trabajo inicial |
| Migraciones incrementales sin recrear la base | Las migraciones aplicadas son inmutables; errores requieren una nueva migración correctiva |
| Compatible con Docker Compose y CI/CD | El equipo debe aprender la convención de nombrado `V{n}__{desc}.sql` |
| `validate` como `ddl-auto` protege producción | Si Hibernate detecta inconsistencia entity/esquema, el arranque falla y debe corregirse |

---

# Impacto en el Sistema

**Backend:** Agregar `spring-boot-starter-flyway` en `pom.xml`; crear `src/main/resources/db/migration/`; convertir scripts SQL actuales en migraciones versionadas; cambiar `ddl-auto` a `validate` en producción.

**Frontend:** Sin impacto.

**Base de datos:** Se crea la tabla `flyway_schema_history` en MySQL; el esquema pasa a ser gestionado exclusivamente por Flyway.

**DevOps:** `docker-compose.yml` no requiere cambios estructurales; las migraciones se ejecutan al arrancar el backend.

**Seguridad:** `auth-seed.sql` → migración Flyway; las credenciales iniciales siguen siendo sensibles y no deben incluirse en repositorios públicos.

**Documentación:** El directorio `db/migration/` documenta implícitamente la evolución del modelo de datos a lo largo del tiempo.

---

# Evidencia (estado actual — base para la propuesta)

* `backend-unab-master/src/main/resources/import.sql`: Script actual sin versionado. Contenido no está en las entradas
* `backend-unab-master/src/main/resources/auth-seed.sql`: Script actual sin versionado. Contenido no está en las entradas
* `docker-compose.yml`: Variable `SPRING_JPA_HIBERNATE_DDL_AUTO` parametrizada; valor `none` por defecto
* `backend-unab-master/pom.xml`: No incluye `spring-boot-starter-flyway` actualmente

---

# Validación

* Confirmar que `spring-boot-starter-flyway` está en `pom.xml`
* Verificar que existe `src/main/resources/db/migration/` con scripts versionados `V{n}__*.sql`
* Confirmar que `flyway_schema_history` existe en MySQL tras el primer arranque
* Verificar que `ddl-auto=validate` no genera errores con el esquema correcto
* Confirmar que una migración nueva se aplica automáticamente al arrancar sin intervención manual
