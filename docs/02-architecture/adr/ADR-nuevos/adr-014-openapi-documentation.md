# ADR-014 - Documentacion formal de la API con OpenAPI 3

## Estado

Propuesto - 2026-05-02

---

# Contexto

El proyecto expone una API REST bajo `/api/*`, pero la documentacion API del repositorio es insuficiente: `docs/05-api/api-documentation.md` esta vacio y no se observa una especificacion OpenAPI generada desde el backend.

---

# Problema

Sin documentacion formal de la API:

* los desarrolladores deben inspeccionar controladores y DTOs para entender el contrato
* el onboarding tecnico es mas lento
* no hay una fuente de verdad clara para endpoints, payloads y respuestas

---

# Decision Arquitectonica

Propuesta futura.

Se propone incorporar una especificacion OpenAPI 3 generada desde el backend para documentar el contrato REST actual del proyecto.

Esta propuesta no depende de introducir versionado `/api/v1/*`. Debe documentar la API realmente existente mientras el sistema use rutas `/api/*`.

---

# Stack tecnologico

| Elemento | Estado actual | Propuesta futura |
|---|---|---|
| Contrato REST | Implicito en codigo | OpenAPI 3 |
| Documentacion API | Archivo vacio o insuficiente | Especificacion generada desde backend |
| Backend | Spring Boot 2.5.3 | Spring Boot 2.5.3 |

---

# Diagrama del stack

```
Controladores + DTOs
        |
        v
Especificacion OpenAPI
        |
        v
Documentacion navegable para equipo y consumo tecnico
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Mantener documentacion manual dispersa | Se desactualiza con facilidad |
| No documentar formalmente la API | Dificulta mantenimiento y onboarding |
| Amarrar la decision a `/api/v1/*` | No esta justificado por el estado actual del proyecto |

---

# Beneficios Arquitectonicos

* Hace visible el contrato REST existente
* Reduce dependencia de leer directamente el codigo para consumir la API
* Mejora trazabilidad entre backend, frontend y documentacion

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Contrato mas claro y util para el equipo | Introduce trabajo adicional de configuracion y mantenimiento |
| Mejor soporte para onboarding | Requiere revisar que la documentacion expuesta no se trate como funcionalidad implementada antes de tiempo |

---

# Impacto en el Sistema

**Backend:** Debe exponer o generar una especificacion OpenAPI si la propuesta se implementa.

**Frontend:** Puede beneficiarse de una referencia formal del contrato, sin cambiar la API actual.

**Documentacion:** Sustituye la dependencia de un archivo vacio o insuficiente por una fuente mas util y trazable.

---

# Evidencia

* `docs/05-api/api-documentation.md`
* `backend-unab-master/src/main/java/com/backend/unab/controllers/`
* `backend-unab-master/src/main/java/com/backend/unab/dto/`
* `backend-unab-master/pom.xml`

---

# Validacion

* Confirmar que la API actual sigue exponiendose bajo `/api/*`
* Confirmar que `docs/05-api/api-documentation.md` sigue siendo insuficiente o vacio
* Requiere validacion antes de implementacion sobre la herramienta exacta a usar para generar OpenAPI
