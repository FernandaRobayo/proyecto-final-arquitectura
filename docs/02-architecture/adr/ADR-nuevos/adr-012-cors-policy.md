# ADR-012 — Política CORS explícita y restrictiva en Spring Security

## Estado

Propuesto — 2026-05-02

---

# Contexto

El frontend (SPA Angular) y el backend (Spring Boot) se ejecutan en orígenes diferentes en producción: el frontend se sirve desde Nginx y el backend expone su API en un contenedor distinto.

CORS (Cross-Origin Resource Sharing) determina qué orígenes tienen permiso para hacer peticiones HTTP al backend desde el navegador. Sin una configuración CORS explícita, el comportamiento depende de la implementación por defecto del framework, que puede ser permisiva o restrictiva según la versión de Spring Security.

---

# Problema

Sin una política CORS documentada y configurada explícitamente:

* el backend puede rechazar peticiones legítimas del frontend si los orígenes no coinciden
* el backend puede aceptar peticiones de cualquier origen, exponiendo la API a consumo no autorizado desde otros dominios
* el comportamiento actual de CORS en Spring Security no está confirmado como restrictivo en las entradas
* en un despliegue cloud o con un dominio externo, la política por defecto puede causar errores difíciles de diagnosticar

---

# Decisión Arquitectónica

Se propone configurar una política CORS explícita y restrictiva directamente en `SpringSecurityConfig.java`:

```java
@Bean
CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // Orígenes permitidos configurables por variable de entorno
    config.setAllowedOrigins(List.of(
        "http://localhost:3000",      // desarrollo local
        "${CORS_ALLOWED_ORIGINS}"     // producción (variable de entorno)
    ));

    config.setAllowedMethods(
        List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
    );
    config.setAllowedHeaders(
        List.of("Authorization", "Content-Type")
    );
    config.setAllowCredentials(false); // HTTP Basic no usa cookies

    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", config);
    return source;
}
```

Los orígenes permitidos se configuran por variable de entorno para soportar distintos entornos sin cambiar código.

---

# Diagrama de la política CORS

```
  Navegador (origen: https://app.dominio.com)
                    │
                    │  OPTIONS /api/customers
                    │  Origin: https://app.dominio.com
                    ▼
        ┌───────────────────────────────────┐
        │      Spring Security CORS         │
        │                                   │
        │  ¿El origen está en la lista?     │
        │                                   │
        │  SÍ → 200 OK                      │
        │       Access-Control-Allow-Origin │
        │                                   │
        │  NO → Error CORS (navegador       │
        │        bloquea la petición)       │
        └───────────────────────────────────┘
```

---

# Alternativas consideradas

| Alternativa | Descripción | Por qué no se eligió |
|---|---|---|
| Permitir todos los orígenes (`*`) | `config.setAllowedOrigins("*")` | Permite que cualquier dominio consuma la API; expone recursos a consumo no autorizado |
| CORS gestionado en Nginx | Agregar headers CORS en la capa Nginx | No protege el backend si se accede directamente; la política debe estar en la capa de seguridad del backend |
| Sin configuración (comportamiento por defecto) | Dejar el default de Spring Security | Comportamiento no documentado; puede cambiar entre versiones de Spring Security |

---

# Beneficios Arquitectónicos

* Los orígenes permitidos son explícitos, auditables y controlados en el repositorio
* Previene que otros dominios consuman la API sin autorización desde el navegador
* La configuración en Spring Security aplica independientemente de cómo se despliega el sistema
* Los orígenes configurables por variable de entorno facilitan distintos entornos (desarrollo, staging, producción)

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Política explícita y auditable en código | Requiere actualizar la lista al cambiar de dominio o agregar nuevos clientes |
| Previene consumo no autorizado desde navegador | CORS solo protege peticiones desde navegador; no reemplaza autenticación para clientes server-side |
| Configurable por variable de entorno | — |

---

# Impacto en el Sistema

**Backend:** Agregar o modificar la configuración CORS en `SpringSecurityConfig.java`; definir variable de entorno `CORS_ALLOWED_ORIGINS`.

**Frontend:** Sin cambios de código. Las peticiones desde el navegador funcionarán si el origen está en la lista permitida.

**Base de datos:** Sin impacto.

**DevOps:** Agregar variable de entorno `CORS_ALLOWED_ORIGINS` en `docker-compose.yml` con el valor correcto por entorno.

**Seguridad:** Reduce la superficie de ataque al limitar qué dominios pueden interactuar con la API desde un navegador.

**Documentación:** La lista de orígenes permitidos debe documentarse y mantenerse actualizada por entorno.

---

# Evidencia (estado actual — base para la propuesta)

* `backend-unab-master/src/main/java/com/backend/unab/auth/SpringSecurityConfig.java`: Configuración actual de Spring Security. Política CORS actual no confirmada en las entradas
* ADR-002 (archivado): Frontend y backend en contenedores distintos implican orígenes distintos en producción

---

# Validación

* Verificar que `SpringSecurityConfig.java` define `CorsConfigurationSource` con orígenes explícitos
* Confirmar que una petición desde un origen no autorizado retorna error 403 o es bloqueada por el navegador
* Verificar que las peticiones del frontend en desarrollo (`http://localhost:3000`) son permitidas
* Confirmar que los orígenes permitidos son configurables por variable de entorno `CORS_ALLOWED_ORIGINS`
