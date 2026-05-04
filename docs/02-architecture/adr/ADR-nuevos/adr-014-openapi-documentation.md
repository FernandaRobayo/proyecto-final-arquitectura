# ADR-014 — Documentación formal de la API con OpenAPI 3 y Springdoc

## Estado

Propuesto — 2026-05-02

---

# Contexto

El sistema no tiene documentación formal del contrato de la API REST. El frontend consume el backend basándose en convenciones implícitas: los servicios Angular deben leer el código de los controladores Spring Boot para conocer los endpoints, parámetros y estructuras de respuesta.

Este acoplamiento implícito dificulta la incorporación de nuevos desarrolladores, la integración con herramientas de prueba automatizada y la evolución controlada del contrato.

---

# Problema

Sin documentación formal de la API:

* un nuevo desarrollador debe leer el código del backend para entender qué endpoints existen y qué retornan
* no existe un contrato verificable entre frontend y backend que detecte incompatibilidades de forma temprana
* las herramientas de prueba (Postman, Insomnia) deben configurarse manualmente sin una fuente de verdad
* en un escenario de múltiples consumidores, cada uno debe descubrir la API de forma independiente
* no hay forma automática de generar clientes tipados para el frontend a partir del contrato

---

# Decisión Arquitectónica

Se propone integrar **Springdoc-openapi** para generar automáticamente la especificación OpenAPI 3 a partir de los controladores Spring Boot:

```
Dependencia     →  springdoc-openapi-ui
Spec JSON       →  GET /v3/api-docs
Spec YAML       →  GET /v3/api-docs.yaml
Swagger UI      →  GET /swagger-ui.html  (interfaz visual interactiva)

Anotaciones opcionales para enriquecer la documentación:
  @OpenAPIDefinition  →  en clase principal (título, versión, descripción)
  @Operation          →  en métodos de controladores
  @Schema             →  en DTOs para describir campos y restricciones
```

La especificación se genera en tiempo de arranque desde el código; es siempre consistente con la implementación real.

---

# Diagrama del flujo de documentación

```
  Desarrollador / Postman / Generador de cliente
                    │
                    │ GET /swagger-ui.html
                    ▼
┌───────────────────────────────────────────────┐
│            Springdoc Swagger UI               │
│                                               │
│  [ GET  /api/v1/customers      ]  ← Try it!  │
│  [ POST /api/v1/customers      ]              │
│  [ PUT  /api/v1/customers/{id} ]              │
│  [ DELETE /api/v1/...          ]              │
└───────────────────────┬───────────────────────┘
                        │  lee
                        ▼
┌───────────────────────────────────────────────┐
│          GET /v3/api-docs                     │
│          Especificación OpenAPI 3 (JSON)      │
│          generada desde controladores y DTOs  │
└───────────────────────────────────────────────┘
```

---

# Alternativas consideradas

| Alternativa | Descripción | Por qué no se eligió |
|---|---|---|
| Documentación manual (README / Confluence) | Escribir endpoints a mano | Se desactualiza rápidamente; no verificable contra la implementación real |
| Springfox | Librería Swagger más antigua para Spring | Springdoc es el sucesor activo; Springfox tiene problemas de compatibilidad con Spring Boot 2.5+ |
| Sin documentación formal | Contratos implícitos | No escala; impide la incorporación eficiente de nuevos desarrolladores e integraciones |

---

# Beneficios Arquitectónicos

* La especificación OpenAPI se genera automáticamente desde el código; siempre sincronizada
* Swagger UI permite probar los endpoints directamente desde el navegador sin herramientas adicionales
* El archivo `api-docs.yaml` puede usarse para generar clientes Angular tipados con Angular OpenAPI Generator
* Facilita la incorporación de nuevos desarrolladores con un catálogo navegable de la API
* Integra directamente con ADR-013 (versionado) para documentar cada versión de la API

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Documentación siempre sincronizada con el código | El endpoint `/swagger-ui.html` debe protegerse o deshabilitarse en producción |
| Permite probar endpoints desde el navegador | Requiere anotaciones adicionales en controladores y DTOs para documentación enriquecida |
| Genera spec exportable para herramientas externas | Springdoc agrega una dependencia al proyecto |
| Compatible con OpenAPI 3 estándar de la industria | — |

---

# Impacto en el Sistema

**Backend:** Agregar `springdoc-openapi-ui` en `pom.xml`; opcionalmente agregar `@Operation` y `@Schema` en controladores y DTOs para enriquecer la documentación.

**Frontend:** Sin impacto de código directo. El archivo `api-docs.yaml` puede usarse para generar servicios Angular automáticamente con Angular OpenAPI Generator.

**Base de datos:** Sin impacto.

**DevOps:** Swagger UI (`/swagger-ui.html`) debe deshabilitarse o protegerse con autenticación en producción mediante propiedad `springdoc.swagger-ui.enabled=false`.

**Seguridad:** La especificación OpenAPI expone la estructura de la API; el acceso a `/v3/api-docs` y `/swagger-ui.html` debe restringirse en producción para evitar reconocimiento de la API por actores externos.

**Documentación:** OpenAPI reemplaza la necesidad de documentar endpoints manualmente; el contrato vive en el código.

---

# Evidencia (estado actual — base para la propuesta)

* `backend-unab-master/pom.xml`: No incluye `springdoc-openapi-ui` actualmente
* ADR-006 (archivado): Documenta el contrato REST implícito entre frontend y backend sin especificación formal

---

# Validación

* Confirmar que `springdoc-openapi-ui` está en `pom.xml`
* Verificar que `GET /v3/api-docs` retorna la especificación OpenAPI 3 en JSON
* Confirmar que `GET /swagger-ui.html` muestra todos los endpoints de la API
* Verificar que en producción el acceso a Swagger UI está restringido o deshabilitado
* Confirmar que los DTOs tienen descripciones suficientes para que la documentación sea comprensible
