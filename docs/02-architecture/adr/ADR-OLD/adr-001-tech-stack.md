# ADR-001 - Seleccion del stack tecnologico principal

## Estado

Aceptado - 2026-05-02

---

# Contexto

El proyecto implementa un sistema web de gestion veterinaria con separacion entre interfaz de usuario, logica de negocio y persistencia. La solucion actual ya esta construida y puede ejecutarse localmente con contenedores.

---

# Problema

Sin una decision explicita sobre el stack tecnologico:

* el mantenimiento del proyecto se vuelve inconsistente entre capas
* la documentacion pierde trazabilidad con la implementacion real
* futuros cambios pueden introducir combinaciones tecnologicas no alineadas con el sistema existente

---

# Decision Arquitectonica

Se adopta el siguiente stack tecnologico, confirmado en el repositorio actual:

```
Frontend                -> Angular 10.2.4
Backend                 -> Spring Boot 2.5.3
Lenguaje API            -> Java 11
Persistencia            -> MySQL 8.0
UI framework            -> Admin-LTE 3.1.0
Publicacion local SPA   -> Nginx en contenedor
```

La publicacion del frontend con Nginx esta confirmada para ejecucion local y despliegue contenedorizado. No se documenta en este ADR como requisito obligatorio para un despliegue productivo en AWS.

Esta decision no responde a un requerimiento academico explicito, sino a una decision tecnica del proyecto orientada a reducir costos operativos al momento de desplegar en AWS. En ese escenario futuro, el frontend puede publicarse como sitio estatico en S3 + CloudFront y Nginx queda como alternativa local o de contenedor.

---

# Stack tecnologico

| Capa | Tecnologia | Version | Evidencia principal |
|---|---|---|---|
| Frontend | Angular | ~10.2.4 | `frontend-unab-master/package.json` |
| Backend | Spring Boot | 2.5.3 | `backend-unab-master/pom.xml` |
| Lenguaje backend | Java | 11 | `backend-unab-master/pom.xml` |
| Base de datos | MySQL | 8.0 | `docker-compose.yml` |
| UI framework | Admin-LTE | ^3.1.0 | `frontend-unab-master/package.json` |
| Publicacion local frontend | Nginx | Imagen `nginx:1.27-alpine` | `frontend-unab-master/Dockerfile` |

---

# Diagrama del stack

```
Frontend (Angular 10 + Admin-LTE)
        |
        | HTTP/JSON
        v
Backend (Spring Boot 2.5.3 / Java 11)
        |
        | JPA / JDBC
        v
Base de datos (MySQL 8.0)
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Otro framework frontend | El proyecto ya esta implementado con Angular 10 y su estructura actual depende de ese stack |
| Otro framework backend | El backend existente ya depende del ecosistema Spring Boot, Spring Security y Spring Data JPA |
| Otro motor relacional | La configuracion actual, los scripts SQL y Docker Compose estan preparados para MySQL 8.0 |
| Publicacion obligatoria con contenedor en AWS | No es necesaria para el frontend si se adopta una estrategia futura de sitio estatico de bajo costo |

---

# Beneficios Arquitectonicos

* Coherencia entre implementacion, despliegue local y documentacion
* Ecosistema maduro para frontend, backend y persistencia
* Compatibilidad directa con Docker Compose para desarrollo local
* Integracion nativa del backend con seguridad y acceso a datos

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Stack conocido y ya integrado | Angular 10 y Spring Boot 2.5.3 no son versiones recientes |
| Separacion clara por capas | Requiere mantener conocimientos en TypeScript y Java |
| Buen soporte de bibliotecas y herramientas | La actualizacion futura de versiones puede requerir trabajo de migracion |
| Nginx simplifica el despliegue local del frontend | No debe confundirse con un requisito obligatorio para produccion AWS |

---

# Impacto en el Sistema

**Backend:** Usa Spring Boot, Spring Security y Spring Data JPA.

**Frontend:** Usa Angular con componentes, rutas, guards e interceptor HTTP.

**Base de datos:** Usa MySQL con esquema definido por scripts SQL.

**DevOps:** El despliegue local se apoya en Docker Compose y Dockerfiles separados.

**Documentacion:** El stack condiciona la forma en que se describen los contenedores y componentes en C4.

---

# Evidencia

* `backend-unab-master/pom.xml`
* `frontend-unab-master/package.json`
* `frontend-unab-master/Dockerfile`
* `docker-compose.yml`
* `README-DOCKER.md`
* `frontend-unab-master/README.md`

---

# Validacion

* Verificar versiones en `backend-unab-master/pom.xml`
* Verificar dependencias frontend en `frontend-unab-master/package.json`
* Confirmar MySQL 8.0 en `docker-compose.yml`
* Confirmar publicacion local del frontend con Nginx en `frontend-unab-master/Dockerfile`
* Confirmar que la opcion AWS de bajo costo aparece solo como decision tecnica futura y no como requisito academico
