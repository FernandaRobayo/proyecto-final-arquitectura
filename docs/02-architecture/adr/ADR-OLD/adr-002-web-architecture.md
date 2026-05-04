# ADR-002 - Arquitectura web de tres capas con SPA, API REST y base de datos

## Estado

Aceptado - 2026-05-02

---

# Contexto

El sistema actual esta desplegado como una aplicacion web separada en frontend, backend y base de datos. Esta organizacion ya esta reflejada en Docker Compose, en los Dockerfiles y en la documentacion C4 del proyecto.

---

# Problema

Sin una separacion formal de capas:

* la interfaz y la logica de negocio quedarian mas acopladas
* seria mas dificil desplegar y mantener cada parte por separado
* la documentacion arquitectonica no reflejaria el runtime real del sistema

---

# Decision Arquitectonica

El sistema se organiza en tres capas de ejecucion:

```
Capa 1 - Frontend web
          Angular 10 compilado y publicado con Nginx

Capa 2 - Backend API
          Spring Boot con endpoints REST bajo /api/*

Capa 3 - Base de datos
          MySQL 8.0 con persistencia relacional
```

En el despliegue local actual, los tres servicios se conectan por la red `unab-network` y ademas publican puertos al host mediante Docker Compose.

---

# Diagrama de arquitectura

```
Usuario (navegador)
        |
        | HTTP
        v
Frontend web (Angular + Nginx)
        |
        | HTTP/JSON
        v
Backend API (Spring Boot)
        |
        | JPA / JDBC
        v
Base de datos MySQL
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Monolito full-stack renderizado en servidor | La implementacion actual ya esta separada en frontend SPA y backend API |
| Microservicios | No hay evidencia de multiples servicios independientes en el proyecto actual |
| Acceso directo del frontend a la base de datos | No corresponde con la implementacion ni con el modelo de seguridad actual |

---

# Beneficios Arquitectonicos

* Separacion clara entre presentacion, logica y persistencia
* Independencia de build entre frontend y backend
* Coherencia con la estructura de contenedores y diagramas C4 actuales

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Componentes separados por responsabilidad | Hay mas artefactos de despliegue que en un monolito unico |
| El frontend puede publicarse como contenido estatico | La integracion frontend-backend requiere resolver CORS y URL base |

---

# Impacto en el Sistema

**Backend:** Expone la API y concentra la logica de negocio.

**Frontend:** Consume la API y no accede directamente a MySQL.

**Base de datos:** Persistencia central del dominio y autenticacion.

**DevOps:** Tres servicios definidos en `docker-compose.yml`.

---

# Evidencia

* `docker-compose.yml`
* `frontend-unab-master/Dockerfile`
* `backend-unab-master/Dockerfile`
* `docs/02-architecture/diagrams/as-is/c4-level-2-containers.md`

---

# Validacion

* Confirmar los tres servicios en `docker-compose.yml`
* Confirmar el flujo `Frontend -> Backend -> MySQL` en la documentacion C4
* Confirmar que el frontend se sirve de forma separada del backend
