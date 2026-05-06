# ADR-010 - Migracion a Flyway para gestion versionada del esquema de base de datos

## Estado

Propuesto - 2026-05-02

---

# Contexto

Actualmente el esquema de base de datos se gestiona mediante `import.sql` y `auth-seed.sql`, ejecutados al arrancar la aplicacion mediante configuracion Spring.

Este mecanismo sirve para bootstrap local y entornos simples, pero no ofrece trazabilidad historica de cambios ni una evolucion incremental del esquema.

---

# Problema

Con scripts SQL de inicializacion como unico mecanismo de gestion de esquema:

* no existe historial versionado de cambios estructurales
* no hay un mecanismo estandar para aplicar solo cambios pendientes
* el mantenimiento del esquema depende de reemplazar o editar scripts base
* es mas dificil evolucionar la base en entornos persistentes

---

# Decision Arquitectonica

Propuesta futura.

Se propone adoptar Flyway como herramienta de migraciones versionadas para que la evolucion del esquema quede trazada en el repositorio y desacoplada de los scripts de bootstrap inicial.

Mientras esta propuesta no se implemente:

* el proyecto sigue dependiendo de `import.sql` y `auth-seed.sql`
* no debe afirmarse que Flyway forma parte del stack actual

---

# Stack tecnologico

| Elemento | Estado actual | Propuesta futura |
|---|---|---|
| Inicializacion de esquema | `import.sql` | Flyway |
| Inicializacion de datos auth | `auth-seed.sql` | Migraciones versionadas o seed controlado |
| ORM | Spring Data JPA / Hibernate | Spring Data JPA / Hibernate |
| Motor de base de datos | MySQL 8.0 | MySQL 8.0 |

---

# Diagrama del stack

```
Estado actual:
Backend -> import.sql + auth-seed.sql -> MySQL

Propuesta futura:
Backend -> Flyway migrations -> MySQL
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Mantener solo `import.sql` y `auth-seed.sql` | No ofrece trazabilidad ni evolucion incremental |
| Migraciones manuales sin herramienta | Depende de procesos informales y es mas dificil de auditar |
| Liquibase | Requiere otra curva de adopcion; no esta justificado por evidencia adicional en el proyecto actual |

---

# Beneficios Arquitectonicos

* Versionado claro de cambios de esquema
* Mejor trazabilidad entre codigo y base de datos
* Menor riesgo al evolucionar entornos que ya tienen datos

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Formaliza la evolucion del esquema | Requiere migrar los scripts actuales a un nuevo formato de trabajo |
| Facilita trazabilidad | Introduce una herramienta adicional al proyecto |

---

# Impacto en el Sistema

**Backend:** Requiere incorporar Flyway y reorganizar la inicializacion de esquema.

**Base de datos:** Cambia la forma de evolucionar el esquema, no el motor relacional.

**Documentacion:** Debe diferenciarse claramente el estado actual basado en scripts SQL de la propuesta futura con migraciones.

---

# Evidencia

* `backend-unab-master/src/main/resources/import.sql`
* `backend-unab-master/src/main/resources/auth-seed.sql`
* `backend-unab-master/src/main/resources/application.properties`
* `backend-unab-master/pom.xml`

---

# Validacion

* Confirmar que Flyway no esta implementado actualmente en `backend-unab-master/pom.xml`
* Confirmar que el proyecto actual depende de `import.sql` y `auth-seed.sql`
* Requiere validacion antes de implementacion sobre como migrar el bootstrap actual a migraciones versionadas
