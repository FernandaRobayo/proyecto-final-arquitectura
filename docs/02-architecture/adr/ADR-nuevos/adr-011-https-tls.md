# ADR-011 — HTTPS obligatorio y terminación TLS en producción

## Estado

Propuesto — 2026-05-02

---

# Contexto

El sistema usa HTTP Basic Authentication donde las credenciales se transmiten en Base64 en cada petición. Base64 no es cifrado; cualquier observador con acceso al tráfico puede decodificar las credenciales en texto claro.

El despliegue actual usa Docker Compose con Nginx sirviendo el frontend por HTTP en el puerto 3000, y Spring Boot exponiendo la API por HTTP en el puerto 9090. No existe configuración de TLS documentada ni implementada en el repositorio.

---

# Problema

Sin HTTPS en producción:

* las credenciales de usuario viajan en Base64 legible por cualquier actor en la red
* un atacante con acceso a la red (MitM, sniffing) puede capturar y reutilizar credenciales
* los datos del dominio viajan sin cifrar entre frontend, backend y usuario
* los navegadores modernos marcan el sitio como "no seguro", degradando la confianza del usuario
* cualquier token o encabezado de sesión puede interceptarse

---

# Decisión Arquitectónica

Se propone implementar terminación TLS en Nginx como punto de entrada único del sistema:

```
Reglas:
  · Nginx termina TLS: gestiona el certificado y expone HTTPS al exterior
  · Redirección obligatoria: todo HTTP → HTTPS (301 Permanent)
  · Backend en red interna: puerto 9090 no expuesto al exterior
  · Acceso externo a la API pasa exclusivamente por Nginx como proxy reverso
  · Certificados: Let's Encrypt (dominio público) o CA interna (intranet)
```

Configuración Nginx objetivo:

```nginx
server {
    listen 443 ssl;
    ssl_certificate     /etc/ssl/certs/cert.pem;
    ssl_certificate_key /etc/ssl/private/key.pem;

    location /api/ {
        proxy_pass http://unab-backend:9090/api/;
    }

    location / {
        try_files $uri $uri/ /index.html;
    }
}

server {
    listen 80;
    return 301 https://$host$request_uri;
}
```

---

# Diagrama de terminación TLS

```
  Usuario (Navegador)
         │
         │ HTTPS :443  (TLS terminado en Nginx)
         ▼
┌────────────────────────────────────────────────┐
│                   Nginx                        │
│                                                │
│  listen 443 ssl;                               │
│  ssl_certificate  /etc/ssl/certs/cert.pem      │
│  ssl_certificate_key /etc/ssl/private/key.pem  │
│                                                │
│  /api/ → proxy_pass http://unab-backend:9090/  │
│  /     → Angular SPA (artefacto estático)      │
│                                                │
│  listen 80; → return 301 https://...           │
└───────────────────────┬────────────────────────┘
                        │  HTTP (red interna unab-network)
                        ▼
           Spring Boot Backend (:9090)
           ← no expuesto al exterior →
```

---

# Alternativas consideradas

| Alternativa | Descripción | Por qué no se eligió |
|---|---|---|
| TLS en Spring Boot directamente | Keystore en la JVM | Posible, pero Nginx como terminador es más eficiente y centraliza la gestión de certificados |
| Migrar a JWT | Reemplazar HTTP Basic por tokens firmados | Mejora el mecanismo de auth pero no elimina la necesidad de HTTPS para proteger el tráfico |
| VPN / red privada exclusiva | Aislar el sistema en red completamente privada | Depende de infraestructura externa; no elimina la necesidad de TLS para cumplimiento |

---

# Beneficios Arquitectónicos

* Las credenciales HTTP Basic viajan cifradas; un observador externo no puede decodificarlas
* Todo el tráfico de datos del dominio queda protegido en tránsito
* Nginx como proxy inverso centraliza la gestión de TLS sin cambiar código del backend
* La redirección 301 garantiza que ningún cliente acceda por HTTP por error
* Compatible con certificados gratuitos Let's Encrypt con renovación automática (certbot)

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Protege las credenciales HTTP Basic en tránsito | Requiere certificado SSL válido y proceso de renovación |
| Nginx proxy inverso unifica el punto de entrada | El puerto 9090 del backend no debe exponerse públicamente (cambio en `docker-compose.yml`) |
| Let's Encrypt automatiza la renovación | Requiere dominio público o CA interna para intranets |
| Mejora la confianza del navegador | Ligero overhead de handshake TLS (insignificante en la práctica) |

---

# Impacto en el Sistema

**Backend:** Sin cambios de código. El puerto 9090 deja de exponerse públicamente en `docker-compose.yml`; el acceso externo pasa exclusivamente por Nginx.

**Frontend:** Sin cambios de código Angular. Solo cambia el protocolo de acceso.

**Base de datos:** Sin impacto.

**DevOps:** `nginx/default.conf` debe configurar bloque SSL, redirección HTTP y `proxy_pass` al backend. `docker-compose.yml` debe montar certificados en el contenedor Nginx.

**Seguridad:** Elimina la vulnerabilidad de credenciales en texto claro. Permite habilitar HSTS (HTTP Strict Transport Security) como mejora adicional.

**Documentación:** `README-DOCKER.md` debe actualizarse con URLs HTTPS y el proceso de gestión de certificados.

---

# Evidencia (estado actual — base para la propuesta)

* `frontend-unab-master/nginx/default.conf`: Configuración Nginx actual sin bloque SSL. Contenido no está en las entradas
* `docker-compose.yml`: Puerto frontend `3000:80`; no existe montaje de certificados SSL
* ADR-005 (archivado): Documenta HTTP Basic como mecanismo de autenticación y señala el riesgo de transmisión sin HTTPS

---

# Validación

* Confirmar que `nginx/default.conf` tiene bloque `listen 443 ssl` con certificado válido
* Verificar que `http://` es redirigido a `https://` con código 301
* Confirmar que el puerto 9090 del backend no es accesible desde el exterior
* Verificar que las credenciales HTTP Basic no son visibles en texto claro al capturar tráfico
* Confirmar que el certificado SSL tiene fecha de expiración futura y renovación automatizada
