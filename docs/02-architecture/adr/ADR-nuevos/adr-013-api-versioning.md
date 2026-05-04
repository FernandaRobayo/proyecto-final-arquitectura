# ADR-013 — Versionado de la API REST con prefijo /api/v1/

## Estado

Propuesto — 2026-05-02

---

# Contexto

La API REST actual expone todos sus endpoints bajo el prefijo `/api/*` sin versionado explícito. El frontend Angular consume estos endpoints directamente.

A medida que el sistema evoluciona, pueden surgir cambios en los contratos de los endpoints (nuevos campos, renombrados, cambios de tipo) que son incompatibles con versiones anteriores del frontend u otros consumidores eventuales.

---

# Problema

Sin versionado de la API:

* cualquier cambio breaking en un endpoint requiere que frontend y backend se desplieguen simultáneamente
* no existe forma de mantener dos versiones del mismo endpoint para una transición gradual
* si en el futuro existen múltiples consumidores (apps móviles, integraciones externas), no hay mecanismo para servirles versiones distintas
* la evolución del contrato de la API no queda trazada de forma explícita en la URL

---

# Decisión Arquitectónica

Se propone adoptar versionado de la API usando el prefijo de ruta `/api/v1/`:

```
Estado actual:
  /api/customers
  /api/appointments
  /api/auth/login

Estado propuesto:
  /api/v1/customers
  /api/v1/appointments
  /api/v1/auth/login
```

Reglas de evolución:

```
· Los controladores usan @RequestMapping("/api/v1/recurso")
· Una versión mayor (v2) se crea cuando hay cambios breaking
· La versión anterior (v1) se mantiene durante un período
  de deprecación antes de eliminarse
· El frontend actualiza api-url.ts para incluir el segmento /v1
```

---

# Diagrama de versionado

```
  Cliente (Angular SPA)
         │
         │ GET /api/v1/customers    ← versión activa
         │ GET /api/v2/customers    ← futura versión con cambios breaking
         ▼
┌────────────────────────────────────────────────┐
│              Spring Boot Backend               │
│                                                │
│  @RestController                               │
│  @RequestMapping("/api/v1/customers")          │
│  class CustomerV1Controller { ... }            │
│                                                │
│  @RestController  ← futura                     │
│  @RequestMapping("/api/v2/customers")          │
│  class CustomerV2Controller { ... }            │
└────────────────────────────────────────────────┘
```

---

# Alternativas consideradas

| Alternativa | Descripción | Por qué no se eligió |
|---|---|---|
| Versionado por encabezado HTTP (`Accept-Version: v1`) | La versión se negocia en el header | Más difícil de probar en navegador y de visualizar en logs; menos estándar para APIs REST |
| Versionado por query param (`?version=1`) | `GET /api/customers?version=1` | Contamina la semántica REST de los recursos |
| Sin versionado (continuar como está) | Evolución sin contrato de versión | Viable a corto plazo; no escala si se agregan consumidores adicionales o el equipo crece |

---

# Beneficios Arquitectónicos

* Los cambios breaking en la API se introducen en v2 sin afectar a los consumidores de v1
* El despliegue de backend y frontend puede desacoplarse durante períodos de transición entre versiones
* La URL explicita la versión del contrato; es visible en logs, trazas y documentación
* Compatible con OpenAPI (ver ADR-014) para documentar cada versión de forma independiente

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Permite evolución del contrato sin romper clientes existentes | Requiere actualizar todas las URLs del frontend en `api-url.ts` y servicios |
| Versionado visible en URL, fácil de trazar en logs | Mantener dos versiones en paralelo duplica controladores temporalmente |
| Facilita la transición gradual entre versiones | Requiere disciplina para deprecar y eliminar versiones antiguas |

---

# Impacto en el Sistema

**Backend:** Actualizar el prefijo en todos los controladores REST de `/api/*` a `/api/v1/*`.

**Frontend:** Actualizar `api-url.ts` para incluir `/api/v1` como base; todos los servicios que usan `BaseResourceService` se actualizan en un solo lugar.

**Base de datos:** Sin impacto.

**DevOps:** Sin impacto en Docker Compose. La URL base puede parametrizarse para facilitar el cambio.

**Seguridad:** Sin impacto directo en la política de seguridad. Las reglas de Spring Security se aplican por prefijo y deben actualizarse a `/api/v1/**`.

**Documentación:** El prefijo versionado debe reflejarse en OpenAPI (ver ADR-014) y en los diagramas C4.

---

# Evidencia (estado actual — base para la propuesta)

* `backend-unab-master/src/main/java/com/backend/unab/controllers/CustomerRestController.java`: Controlador actual con prefijo `/api/` sin versión. Contenido no está en las entradas
* `frontend-unab-master/src/app/utils/api-url.ts`: URL base actual sin segmento de versión. Contenido no está en las entradas
* ADR-006 (archivado): Documenta el prefijo `/api/*` sin versionado

---

# Validación

* Verificar que todos los controladores usan el prefijo `/api/v1/`
* Confirmar que `api-url.ts` incluye el segmento `/v1` en la URL base
* Verificar que las rutas originales `/api/*` sin versión ya no existen o retornan 404
* Confirmar que las reglas de Spring Security cubren el nuevo prefijo `/api/v1/**`
