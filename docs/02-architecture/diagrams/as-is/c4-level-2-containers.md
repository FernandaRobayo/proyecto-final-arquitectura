# Nivel 2 - Contenedores

## Descripcion

Este diagrama descompone el sistema actual en sus contenedores reales confirmados en `docker-compose.yml`, Dockerfiles y configuracion de ejecucion.

## Diagrama

- [c4-level-2-containers.puml](C:/www/proyecto-final-arquitectura/docs/02-architecture/diagrams/as-is/c4-level-2-containers.puml)

## Elementos

- `Usuario web`
- `Frontend web`
  Angular 10 compilado y servido con Nginx.
- `Backend API`
  Spring Boot 2.5.3 con Java 11.
- `Base de datos MySQL`
  MySQL 8.0 para persistencia del dominio y autenticacion.

## Relaciones

- `Usuario web -> Frontend web`
  Usa la interfaz desde el navegador por HTTP en `localhost:3000`.
- `Frontend web -> Backend API`
  Consume `POST /api/auth/login` y los endpoints REST bajo `/api/*` por HTTP/JSON en el puerto `9090`.
- `Backend API -> Base de datos MySQL`
  Lee y escribe datos mediante JPA/Hibernate sobre MySQL.

## Observaciones

- El frontend se publica como SPA y Nginx redirige rutas a `index.html`.
- El backend inicializa esquema y datos SQL desde archivos de recursos.
- No se incluyen contenedores adicionales porque no existen microservicios ni infraestructura externa confirmada.
