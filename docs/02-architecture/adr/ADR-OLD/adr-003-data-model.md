# ADR-003 - Persistencia relacional con MySQL y Spring Data JPA

## Estado

Aceptado - 2026-05-02

---

# Contexto

El sistema maneja entidades relacionadas del dominio veterinario y usa una base de datos relacional con acceso desde Spring Boot.

---

# Problema

Sin una estrategia de persistencia definida:

* el acceso a datos seria inconsistente
* la integridad entre entidades del dominio quedaria menos controlada
* el arranque de entornos nuevos seria menos reproducible

---

# Decision Arquitectonica

La persistencia actual se apoya en:

```
Motor        -> MySQL 8.0
ORM          -> Spring Data JPA con Hibernate
Acceso       -> Interfaces DAO que extienden JpaRepository
Esquema      -> import.sql
Datos auth   -> auth-seed.sql
Control DDL  -> SPRING_JPA_HIBERNATE_DDL_AUTO por variable de entorno
               valor por defecto: none
Inicializacion SQL -> spring.sql.init.schema-locations y data-locations
```

---

# Diagrama de la capa de persistencia

```
Controller -> Service -> DAO -> Entity
                           |
                           v
                     MySQL 8.0

Esquema: import.sql
Seed de autenticacion: auth-seed.sql
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Base no relacional | El dominio actual tiene relaciones que encajan con un modelo relacional |
| Acceso JDBC manual sin JPA | El proyecto ya usa repositorios Spring Data JPA |
| Migraciones versionadas | No estan implementadas hoy; solo aparecen como propuesta en ADR nuevos |

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| JPA reduce boilerplate CRUD | El control de cambios de esquema no esta versionado con una herramienta dedicada |
| MySQL es coherente con Docker Compose actual | La evolucion del esquema depende hoy de scripts SQL mantenidos manualmente |

---

# Impacto en el Sistema

**Backend:** Repositorios JPA y entidades anotadas con JPA.

**Base de datos:** MySQL 8.0 con esquema y seed iniciales.

**DevOps:** Variables de datasource y `ddl-auto` definidas por entorno.

**Seguridad:** `auth-seed.sql` inicializa usuarios y roles.

---

# Evidencia

* `backend-unab-master/pom.xml`
* `backend-unab-master/src/main/resources/application.properties`
* `backend-unab-master/src/main/resources/import.sql`
* `backend-unab-master/src/main/resources/auth-seed.sql`
* `backend-unab-master/src/main/java/com/backend/unab/models/dao/ICustomerDao.java`

---

# Validacion

* Verificar JPA y MySQL en `backend-unab-master/pom.xml`
* Confirmar `spring.sql.init.*` en `application.properties`
* Confirmar existencia de `import.sql` y `auth-seed.sql`
