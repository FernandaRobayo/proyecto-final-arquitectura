# ADR candidatos recomendados para el proyecto

Este documento consolida los ADR candidatos identificados a partir de la implementacion actual del proyecto, su configuracion y la documentacion disponible.

## ADR-001 - Adoptar una arquitectura web de tres capas con SPA frontend, API backend y base de datos relacional

### Estado sugerido

Confirmado

### Decision que deberia documentarse

El sistema esta implementado como una arquitectura separada en:

- frontend web SPA
- backend API
- base de datos relacional MySQL

### Evidencia encontrada

- `docker-compose.yml`
- `frontend-unab-master/Dockerfile`
- `backend-unab-master/Dockerfile`
- `docs/02-architecture/diagrams/as-is/c4-level-2-containers.md`

### Razon para crear el ADR

Es la decision arquitectonica principal del sistema y condiciona despliegue, integracion, seguridad y evolucion futura.

### Alternativas que podrian mencionarse

- Monolito full-stack
- Aplicacion server-rendered
- Microservicios

### Impacto arquitectonico

Afecta separacion de responsabilidades, despliegue, mantenimiento y evolucion del sistema.

### Prioridad

Alta

---

## ADR-002 - Usar Angular 10 como SPA servida por Nginx para la interfaz web

### Estado sugerido

Confirmado

### Decision que deberia documentarse

El frontend esta construido como SPA en Angular 10 y se publica en produccion dentro de un contenedor Nginx con fallback a `index.html`.

### Evidencia encontrada

- `frontend-unab-master/package.json`
- `frontend-unab-master/angular.json`
- `frontend-unab-master/nginx/default.conf`
- `frontend-unab-master/Dockerfile`

### Razon para crear el ADR

Explica por que el frontend se comporta como SPA, como se sirve en runtime y que supuestos existen para routing y despliegue.

### Alternativas que podrian mencionarse

- Angular servido por Node
- SSR
- Otra tecnologia frontend

### Impacto arquitectonico

Impacta despliegue, routing del frontend, build pipeline y operacion en contenedores.

### Prioridad

Alta

---

## ADR-003 - Usar comunicacion REST sobre HTTP/JSON entre frontend y backend

### Estado sugerido

Confirmado

### Decision que deberia documentarse

La comunicacion entre frontend y backend se realiza mediante API REST por HTTP/JSON, con el frontend resolviendo el host del backend dinamicamente y consumiendo endpoints `/api/*`.

### Evidencia encontrada

- `frontend-unab-master/src/app/views/login/login.service.ts`
- `frontend-unab-master/src/app/services/base-resource.service.ts`
- `frontend-unab-master/src/app/utils/api-url.ts`
- `backend-unab-master/src/main/java/com/backend/unab/controllers/CustomerRestController.java`
- `backend-unab-master/src/main/java/com/backend/unab/controllers/AppointmentRestController.java`

### Razon para crear el ADR

Es una decision transversal que explica la integracion entre capas y el contrato de comunicacion principal del sistema.

### Alternativas que podrian mencionarse

- GraphQL
- gRPC
- Backend-for-frontend
- Server-side rendering con llamadas internas

### Impacto arquitectonico

Impacta compatibilidad, trazabilidad de endpoints, pruebas, acoplamiento entre frontend y backend y evolucion de la API.

### Prioridad

Alta

---

## ADR-004 - Usar MySQL relacional con Spring Data JPA/Hibernate y scripts SQL para esquema y datos iniciales

### Estado sugerido

Confirmado

### Decision que deberia documentarse

La persistencia se resuelve con MySQL 8.0 como base de datos relacional, acceso mediante Spring Data JPA/Hibernate y bootstrap del esquema/datos por `import.sql` y `auth-seed.sql`, con `ddl-auto` parametrizado y por defecto en `none`.

### Evidencia encontrada

- `backend-unab-master/pom.xml`
- `backend-unab-master/src/main/resources/application.properties`
- `backend-unab-master/src/main/resources/import.sql`
- `backend-unab-master/src/main/resources/auth-seed.sql`
- `backend-unab-master/src/main/java/com/backend/unab/models/dao/ICustomerDao.java`

### Razon para crear el ADR

Documenta una combinacion importante de decisiones de persistencia: modelo relacional, ORM y estrategia de inicializacion del esquema.

### Alternativas que podrian mencionarse

- Generacion automatica con Hibernate
- Migraciones con Flyway/Liquibase
- Base documental o no relacional

### Impacto arquitectonico

Impacta evolucion del modelo de datos, portabilidad, control de cambios de esquema y despliegue reproducible.

### Prioridad

Alta

---

## ADR-005 - Usar autenticacion stateless con Spring Security, HTTP Basic y roles internos `ROLE_ADMIN` y `ROLE_STAFF`

### Estado sugerido

Confirmado

### Decision que deberia documentarse

La seguridad actual usa Spring Security con sesiones stateless, `httpBasic()`, login en `/api/auth/login`, token `Authorization` de tipo `Basic` devuelto al frontend y dos roles internos implementados: `ROLE_ADMIN` y `ROLE_STAFF`.

### Evidencia encontrada

- `backend-unab-master/src/main/java/com/backend/unab/auth/SpringSecurityConfig.java`
- `backend-unab-master/src/main/java/com/backend/unab/controllers/AuthRestController.java`
- `backend-unab-master/src/main/java/com/backend/unab/models/services/UsuarioService.java`
- `backend-unab-master/src/main/resources/auth-seed.sql`
- `frontend-unab-master/src/app/interceptors/auth.interceptor.ts`

### Razon para crear el ADR

Es una decision critica para seguridad y operacion. Tambien aclara que el proyecto actual si implementa roles, pero no se confirmo autorizacion fina por endpoint basada en `hasRole`.

### Alternativas que podrian mencionarse

- JWT
- OAuth2/OIDC
- Sesiones stateful
- Roles y permisos mas granulares

### Impacto arquitectonico

Impacta seguridad, integracion frontend-backend, gestion de credenciales y evolucion de autorizacion.

### Prioridad

Alta

---

## ADR-006 - Organizar el backend con arquitectura en capas y contratos DTO para la API

### Estado sugerido

Confirmado

### Decision que deberia documentarse

El backend sigue una organizacion por controladores REST, servicios, repositorios y entidades, exponiendo DTOs hacia la API y centralizando errores con `RestControllerAdvice`.

### Evidencia encontrada

- `backend-unab-master/src/main/java/com/backend/unab/controllers/CustomerRestController.java`
- `backend-unab-master/src/main/java/com/backend/unab/models/services/CustomerServiceImpl.java`
- `backend-unab-master/src/main/java/com/backend/unab/models/dao/ICustomerDao.java`
- `backend-unab-master/src/main/java/com/backend/unab/dto/CustomerDto.java`
- `backend-unab-master/src/main/java/com/backend/unab/exception/GlobalExceptionHandler.java`

### Razon para crear el ADR

Explica el patron dominante del backend y evita que nuevos cambios mezclen responsabilidades o expongan entidades directamente.

### Alternativas que podrian mencionarse

- Exponer entidades JPA directamente
- Arquitectura hexagonal
- Modulos por feature mas autonomos

### Impacto arquitectonico

Impacta mantenibilidad, legibilidad, desacoplamiento de la API y consistencia en nuevos modulos.

### Prioridad

Alta

---

## ADR-007 - Implementar un patron frontend generico y dirigido por metadata para CRUD de recursos del dominio

### Estado sugerido

Confirmado

### Decision que deberia documentarse

El frontend no implementa una pantalla CRUD totalmente distinta por cada recurso; usa componentes genericos (`ResourceIndexComponent`, `ResourceFormComponent`), servicios reutilizables y un `ResourceRegistryService` que describe rutas, columnas, campos y servicios por recurso.

### Evidencia encontrada

- `frontend-unab-master/src/app/views/resources/resource-index/resource-index.component.ts`
- `frontend-unab-master/src/app/views/resources/resource-form/resource-form.component.ts`
- `frontend-unab-master/src/app/services/base-resource.service.ts`
- `frontend-unab-master/src/app/services/resource-registry.service.ts`
- `frontend-unab-master/src/app/app-routing.module.ts`

### Razon para crear el ADR

Es una decision de diseño relevante que afecta como se agregan nuevos modulos, como se comparte UI y que trade-offs existen entre flexibilidad y reutilizacion.

### Alternativas que podrian mencionarse

- Componentes CRUD especificos por entidad
- Generacion de formularios con otra libreria
- Microfrontends por modulo

### Impacto arquitectonico

Impacta mantenibilidad del frontend, velocidad de incorporacion de nuevos recursos y uniformidad de experiencia.

### Prioridad

Media

---

## ADR-008 - Adoptar documentacion arquitectonica basada en C4 y separar vistas `as-is` y `to-be`

### Estado sugerido

Confirmado

### Decision que deberia documentarse

El proyecto organiza la documentacion arquitectonica usando C4, con vistas `as-is` y `to-be`, y ademas preve un directorio especifico para ADR.

### Evidencia encontrada

- `docs/02-architecture/diagrams/as-is/c4-level-1-context.md`
- `docs/02-architecture/diagrams/as-is/c4-level-2-containers.md`
- `docs/02-architecture/diagrams/as-is/c4-level-3-components.md`
- `docs/02-architecture/diagrams/to-be/`
- `docs/02-architecture/adr/`

### Razon para crear el ADR

La forma de documentar arquitectura tambien es una decision arquitectonica; conviene dejar explicito que modelo se usa y como se mantiene.

### Alternativas que podrian mencionarse

- Diagramas ad hoc sin estandar
- UML tradicional
- Documentacion textual sin vistas separadas

### Impacto arquitectonico

Impacta onboarding, trazabilidad de decisiones y alineacion entre implementacion y documentacion.

### Prioridad

Media

---

## ADR-009 - Definir una arquitectura de despliegue en AWS

### Estado sugerido

Requiere validacion

### Decision que deberia documentarse

Posible ADR para despliegue cloud en AWS.

### Evidencia encontrada

- `docs/02-architecture/adr/adr-004-aws-deployment.md`
- `docs/06-aws/aws-architecture.md`
- `docs/06-aws/aws-access-tree.md`
- `docs/06-aws/aws-costs.md`

### Razon para crear el ADR

El tema ya fue previsto en la documentacion, pero no se confirmo en codigo, IaC ni configuracion de despliegue real.

### Alternativas que podrian mencionarse

- Mantener solo despliegue local con Docker Compose
- Otro proveedor cloud
- PaaS o VM simples

### Impacto arquitectonico

Seria alto si realmente existe una estrategia cloud, pero no esta confirmado en el proyecto actual.

### Prioridad

Baja
