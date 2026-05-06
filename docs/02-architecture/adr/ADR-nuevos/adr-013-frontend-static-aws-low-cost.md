# ADR-013 - Estrategia de despliegue de bajo costo para el frontend en AWS

## Estado

Propuesto - 2026-05-02

---

# Contexto

El frontend es una SPA Angular que en el entorno local actual se construye y publica con Nginx dentro de un contenedor.

La documentacion actual del proyecto tambien describe una opcion futura de despliegue de menor costo en AWS basada en sitio estatico:

* `README-DOCKER.md`
* `frontend-unab-master/README.md`
* `environment.prod.ts` sin host absoluto para API

---

# Problema

Si el frontend se despliega en AWS manteniendo una estrategia contenedorizada obligatoria:

* pueden introducirse costos operativos innecesarios para una SPA estatica
* se sobredimensiona la infraestructura de publicacion del frontend
* se pierde la ventaja de separar el frontend estatico del backend

---

# Decision Arquitectonica

Propuesta futura.

Para un despliegue futuro del frontend en AWS, la opcion preferente del proyecto sera:

* compilar Angular como artefacto estatico
* publicar los archivos estaticos en S3
* usar CloudFront como distribucion y punto de entrega
* resolver `/api/*` hacia el backend desde la capa de infraestructura o usar configuracion equivalente

Nginx en contenedor se mantiene como alternativa local o de despliegue contenedorizado, pero no como requisito obligatorio para produccion AWS.

Esta decision no responde a un requerimiento academico explicito, sino a una decision tecnica del proyecto orientada a reducir costos operativos al momento de desplegar en AWS.

---

# Stack tecnologico

| Elemento | Estado actual | Propuesta futura |
|---|---|---|
| Frontend | Angular 10 | Angular 10 |
| Publicacion local | Nginx en contenedor | Sin cambio obligatorio |
| Publicacion AWS de bajo costo | No implementada | S3 + CloudFront |
| Resolucion API en produccion | Base relativa vacia en `environment.prod.ts` | `/api/*` resuelto por infraestructura equivalente |

---

# Diagrama del stack

```
Local actual:
Angular build -> Nginx container -> navegador

Propuesta futura AWS:
Angular build -> S3 + CloudFront -> navegador
                         |
                         +-> /api/* hacia backend o equivalente
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Mantener Nginx contenedorizado como unica opcion | Sobredimensiona el frontend para una SPA estatica |
| Publicar frontend con host absoluto fijo hacia backend | Reduce flexibilidad y acopla el build al entorno |
| No documentar una estrategia futura | Deja ambigua una decision de despliegue ya insinuada en el proyecto |

---

# Beneficios Arquitectonicos

* Reduce costos potenciales de publicacion del frontend
* Aprovecha que Angular genera artefactos estaticos
* Mantiene separacion entre despliegue local y despliegue productivo futuro

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Menor costo operativo potencial en AWS | Requiere configurar correctamente routing SPA y resolucion de `/api/*` |
| Menor dependencia de contenedores para el frontend | Introduce una variante adicional de despliegue a documentar |

---

# Impacto en el Sistema

**Frontend:** No cambia la aplicacion Angular; cambia la estrategia de publicacion futura.

**Backend:** Debe poder resolverse detras de `/api/*` o configuracion equivalente en produccion.

**DevOps:** Mantiene Docker Compose para local y habilita una alternativa futura de menor costo en AWS.

**Documentacion:** Debe dejar claro que se trata de una decision tecnica del proyecto, no de un requisito academico.

---

# Evidencia

* `README-DOCKER.md`
* `frontend-unab-master/README.md`
* `frontend-unab-master/src/environments/environment.prod.ts`
* `frontend-unab-master/src/app/utils/api-url.ts`
* `frontend-unab-master/Dockerfile`

---

# Validacion

* Confirmar que el frontend actual ya puede usar base relativa en produccion
* Confirmar que la opcion `S3 + CloudFront` esta documentada en el proyecto
* Requiere validacion antes de implementacion sobre la infraestructura exacta que resolvera `/api/*`
