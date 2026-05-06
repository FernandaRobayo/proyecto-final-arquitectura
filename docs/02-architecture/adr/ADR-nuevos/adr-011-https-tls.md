# ADR-011 - HTTPS obligatorio y terminacion TLS en despliegues reales

## Estado

Propuesto - 2026-05-02

---

# Contexto

El sistema usa HTTP Basic Authentication. En ese mecanismo las credenciales se transportan en Base64, por lo que requieren un canal cifrado para no quedar expuestas durante el trafico real.

El despliegue actual documentado en el repositorio es local con Docker Compose, frontend por HTTP y backend por HTTP. No hay configuracion TLS implementada en el proyecto actual.

---

# Problema

Sin HTTPS en despliegues reales:

* las credenciales HTTP Basic pueden ser capturadas en transito
* los datos del sistema viajan sin cifrado
* se debilita la postura minima de seguridad del proyecto

---

# Decision Arquitectonica

Propuesta futura.

Se propone exigir HTTPS para cualquier despliegue real del sistema y delegar la terminacion TLS en el punto de entrada que resulte adecuado para el entorno:

* CloudFront, si el frontend se publica como sitio estatico en AWS
* ALB o API Gateway, si la infraestructura futura lo requiere
* Nginx, solo si se usa un despliegue contenedorizado con proxy frontal

No se define Nginx como unico terminador TLS.

Esta decision no responde a un requerimiento academico explicito, sino a una decision tecnica del proyecto orientada a reducir costos operativos al momento de desplegar en AWS.

---

# Stack tecnologico

| Elemento | Estado actual | Propuesta futura |
|---|---|---|
| Autenticacion | HTTP Basic | HTTP Basic protegido por HTTPS |
| Frontend local | Angular servido con Nginx por HTTP | Sin cambio local obligatorio |
| Entrada productiva | No confirmada en el proyecto actual | CloudFront, ALB, API Gateway o Nginx segun entorno |

---

# Diagrama del stack

```
Usuario
   |
   | HTTPS
   v
Punto de entrada del entorno
   |
   +-- CloudFront / ALB / API Gateway / Nginx
   |
   v
Frontend y/o backend segun arquitectura del despliegue
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Mantener HTTP en despliegues reales | No protege credenciales ni datos en transito |
| Definir Nginx como unica opcion | No se alinea con la estrategia futura de AWS bajo costo ni con despliegues alternativos |
| Cambiar primero el mecanismo de autenticacion y posponer TLS | No resuelve el riesgo de transporte en el corto plazo |

---

# Beneficios Arquitectonicos

* Protege credenciales y datos en transito
* Reduce riesgo operativo del uso actual de HTTP Basic
* Permite adaptar la terminacion TLS al entorno real de despliegue

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Mejora inmediata de seguridad en despliegues reales | Requiere definir y operar certificados en el entorno elegido |
| No acopla la decision a una sola herramienta | La implementacion concreta depende de infraestructura futura |

---

# Impacto en el Sistema

**Frontend:** Sin cambio funcional obligatorio; cambia la forma de publicacion en entornos reales.

**Backend:** Debe quedar protegido detras de un punto de entrada HTTPS si se expone fuera del entorno local.

**DevOps:** Requiere definir el terminador TLS segun el entorno real de despliegue.

**Seguridad:** Es una mejora prioritaria mientras se mantenga HTTP Basic.

---

# Evidencia

* `backend-unab-master/src/main/java/com/backend/unab/auth/SpringSecurityConfig.java`
* `backend-unab-master/src/main/java/com/backend/unab/controllers/AuthRestController.java`
* `frontend-unab-master/nginx/default.conf`
* `docker-compose.yml`
* `README-DOCKER.md`
* `frontend-unab-master/README.md`

---

# Validacion

* Confirmar que el proyecto actual no implementa TLS en el repositorio
* Confirmar que HTTP Basic sigue siendo el mecanismo actual
* Requiere validacion antes de implementacion sobre que punto de entrada asumira TLS en el entorno real
