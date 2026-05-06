# ADR-012 - Politica CORS explicita y restrictiva por ambiente

## Estado

Propuesto - 2026-05-02

---

# Contexto

El backend y el frontend pueden ejecutarse en origenes distintos durante desarrollo local. En el codigo actual, la politica CORS observada es permisiva:

* `SpringSecurityConfig` permite `allowedOrigins("*")`
* los controladores revisados usan `@CrossOrigin(origins = "*")`

En un despliegue futuro same-origin, por ejemplo frontend publicado con una ruta publica que resuelve `/api/*`, la necesidad de CORS cambia. Por eso la politica debe ser explicita y dependiente del ambiente.

---

# Problema

Sin una politica CORS documentada y controlada por ambiente:

* el backend puede quedar innecesariamente abierto a origenes arbitrarios
* se mezclan necesidades de desarrollo local con despliegues reales
* la seguridad del acceso desde navegador depende de defaults permisivos

---

# Decision Arquitectonica

Propuesta futura.

Se propone definir una politica CORS explicita y parametrizable por ambiente:

* desarrollo local: permitir solo los origenes necesarios del frontend local
* despliegues same-origin: reducir o eliminar la necesidad de CORS si frontend y backend comparten dominio publico
* otros despliegues: permitir solo origenes explicitamente aprobados

No debe afirmarse que esta politica restrictiva ya existe en el proyecto actual.

---

# Stack tecnologico

| Elemento | Estado actual | Propuesta futura |
|---|---|---|
| CORS en seguridad | `allowedOrigins("*")` | Lista explicita por ambiente |
| CORS en controladores | `@CrossOrigin(origins = "*")` | Uso consistente y restringido o centralizado |
| Despliegue frontend/backend | Local con origenes distintos; futuro same-origin posible | Configuracion segun ambiente |

---

# Diagrama del stack

```
Desarrollo local:
Frontend local -> Backend con CORS permitido para origenes necesarios

Despliegue same-origin:
Frontend publico -> /api/* -> Backend
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Mantener `*` en todos los ambientes | Es demasiado permisivo para despliegues reales |
| No configurar CORS explicitamente | Mantiene ambiguedad y dependencia del estado actual del codigo |
| Resolver CORS solo en infraestructura | No reemplaza la necesidad de una politica clara en el backend |

---

# Beneficios Arquitectonicos

* Alinea seguridad del navegador con el entorno real
* Evita configurar permisos mas amplios de los necesarios
* Separa correctamente desarrollo local de despliegues reales

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Menor superficie expuesta desde navegador | Requiere mantener configuracion por ambiente |
| Politica mas clara y auditable | Necesita revisar tanto configuracion central como anotaciones en controladores |

---

# Impacto en el Sistema

**Backend:** Requiere revisar `SpringSecurityConfig` y el uso de `@CrossOrigin`.

**Frontend:** Sin cambio funcional directo; solo debe operar desde origenes permitidos.

**DevOps:** Debe definir origenes segun ambiente cuando aplique.

---

# Evidencia

* `backend-unab-master/src/main/java/com/backend/unab/auth/SpringSecurityConfig.java`
* `backend-unab-master/src/main/java/com/backend/unab/controllers/AuthRestController.java`
* `backend-unab-master/src/main/java/com/backend/unab/controllers/CustomerRestController.java`
* `README-DOCKER.md`
* `frontend-unab-master/README.md`

---

# Validacion

* Confirmar que el estado actual es permisivo en `SpringSecurityConfig.java`
* Confirmar si los controladores siguen usando `@CrossOrigin(origins = "*")`
* Requiere validacion antes de implementacion sobre los ambientes reales que deban soportarse
