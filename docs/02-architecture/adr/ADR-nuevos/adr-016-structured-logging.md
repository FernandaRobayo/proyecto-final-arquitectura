# ADR-016 — Logging estructurado con correlación de peticiones

## Estado

Propuesto — 2026-05-02

---

# Contexto

Spring Boot incluye Logback como framework de logging por defecto, configurado para salida en texto plano a la consola. Docker captura esta salida estándar y la expone via `docker compose logs`.

El formato de texto plano es legible para humanos en desarrollo, pero dificulta el análisis automatizado, el filtrado por campo y la correlación de múltiples líneas de log pertenecientes a una misma petición HTTP en entornos concurrentes.

---

# Problema

Con logging en texto plano sin correlación de peticiones:

* es imposible filtrar automáticamente todos los logs de una sola petición entre los logs de peticiones concurrentes
* un error en producción requiere leer líneas de log manualmente para reconstruir el flujo completo
* no es posible integrar con herramientas de observabilidad (ELK Stack, Grafana Loki, CloudWatch) sin transformación adicional
* no existe un identificador único (`correlationId`) que relacione los logs del backend con las peticiones del frontend

---

# Decisión Arquitectónica

Se propone adoptar **logging estructurado en formato JSON** con correlación de peticiones usando MDC:

```
Formato        →  JSON (logstash-logback-encoder)
Correlación    →  MDC (Mapped Diagnostic Context) con X-Correlation-ID
                  por petición HTTP
Campos mínimos →  timestamp · level · logger · message
                  correlationId · method · path · statusCode
Destino        →  stdout (Docker captura y reenvía al driver de logging)
```

Implementación del filtro de correlación:

```java
@Component
public class CorrelationFilter implements Filter {
    public void doFilter(
            ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        String correlationId = Optional
            .ofNullable(httpReq.getHeader("X-Correlation-ID"))
            .orElse(UUID.randomUUID().toString());

        MDC.put("correlationId", correlationId);
        try {
            chain.doFilter(req, res);
        } finally {
            MDC.clear();  // evita fuga de contexto en threads reutilizados
        }
    }
}
```

---

# Diagrama del flujo de logging

```
  Petición HTTP → CorrelationFilter
                        │
                        │  MDC.put("correlationId", "abc-123")
                        ▼
            ┌───────────────────────────┐
            │  Controller               │  log.info("...")
            │  Service                  │  log.debug("...")
            │  DAO                      │  log.error("...")
            └───────────┬───────────────┘
                        │  cada log incluye correlationId del MDC
                        ▼
            ┌───────────────────────────┐
            │  Logback + JSON encoder   │
            │                           │
            │  {                        │
            │    "timestamp": "...",    │
            │    "level": "INFO",       │
            │    "message": "...",      │
            │    "correlationId":       │
            │      "abc-123"            │
            │  }                        │
            └───────────┬───────────────┘
                        │ stdout
                        ▼
            docker compose logs
            filtrar por correlationId
```

---

# Alternativas consideradas

| Alternativa | Descripción | Por qué no se eligió |
|---|---|---|
| Mantener logging texto plano actual | Sin cambios en la configuración de Logback | Dificulta el análisis automatizado y la correlación; no escala con el volumen de logs |
| OpenTelemetry / Jaeger (tracing distribuido) | Instrumentación completa de trazas distribuidas | Mayor complejidad; adecuado para múltiples servicios; puede adoptarse como evolución futura desde esta base |
| Logging centralizado solo en Nginx | Capturar solo las peticiones HTTP en Nginx | No incluye la perspectiva del backend ni los errores internos de la aplicación |

---

# Beneficios Arquitectónicos

* Todos los logs de una petición comparten el mismo `correlationId`; se pueden filtrar y agrupar con precisión
* El formato JSON es consumible directamente por ELK Stack, Grafana Loki, AWS CloudWatch Logs y similares
* Los logs estructurados permiten crear alertas basadas en campos (ej: alertar cuando `level=ERROR` supera un umbral)
* La implementación es incremental: se puede adoptar sin cambiar la lógica de negocio existente

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Correlación exacta de todos los logs de una petición | Los logs en JSON son menos legibles directamente en consola para desarrollo |
| Integrable con herramientas de observabilidad | Requiere agregar `logstash-logback-encoder` como dependencia |
| Alertas y métricas basadas en campos de log | El `CorrelationFilter` es una dependencia transversal del backend |
| Preparado para evolución a tracing distribuido | — |

---

# Impacto en el Sistema

**Backend:** Agregar dependencia `logstash-logback-encoder`; configurar `logback-spring.xml` para formato JSON; implementar `CorrelationFilter`; configurar MDC en todos los filtros relevantes.

**Frontend:** Opcional: generar un `X-Correlation-ID` en el interceptor Angular y adjuntarlo a las peticiones para trazar de extremo a extremo.

**Base de datos:** Sin impacto.

**DevOps:** `docker compose logs` sigue funcionando; se habilita la posibilidad de reenviar stdout a un colector de logs (ELK, Loki, CloudWatch).

**Seguridad:** Los logs no deben incluir credenciales, contraseñas ni el valor del encabezado `Authorization`. El `CorrelationFilter` no debe loguear el header de autenticación.

**Documentación:** Documentar los campos estándar de los logs JSON y cómo filtrar por `correlationId` para diagnóstico de incidentes.

---

# Evidencia (estado actual — base para la propuesta)

* `backend-unab-master/pom.xml`: Incluye `spring-boot-starter` con Logback por defecto; no se evidencia `logstash-logback-encoder`
* No se encontró configuración `logback-spring.xml` en el repositorio
* `docker-compose.yml`: Los contenedores usan el driver de logging por defecto de Docker (json-file)

---

# Validación

* Verificar que los logs del backend son emitidos en formato JSON con campos `timestamp`, `level`, `message`, `correlationId`
* Confirmar que todos los logs de una misma petición HTTP comparten el mismo `correlationId`
* Verificar que el `CorrelationFilter` no incluye el valor del encabezado `Authorization` en los logs
* Confirmar que el formato JSON es válido y parseable: `docker compose logs backend | jq .`
