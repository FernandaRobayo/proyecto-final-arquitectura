# ADR-016 - Logging consistente con correlacion de peticiones

## Estado

Propuesto - 2026-05-02

---

# Contexto

El backend usa el logging por defecto de Spring Boot hacia salida estandar. No se observa una politica documentada sobre formato de logs, uso de correlation ID o exclusion explicita de datos sensibles.

---

# Problema

Sin una politica de logging consistente:

* es mas dificil seguir el flujo de una peticion entre varias capas
* el diagnostico de incidentes depende de mensajes dispersos
* existe riesgo de registrar datos sensibles sin una regla clara

---

# Decision Arquitectonica

Propuesta futura.

Se propone adoptar una politica de logging consistente para el backend con estas reglas minimas:

* emitir logs por `stdout`
* usar niveles de log coherentes
* evitar registrar credenciales o secretos
* introducir un identificador de correlacion por peticion si el equipo valida su implementacion

El uso de herramientas externas de observabilidad queda fuera del alcance de esta decision y, si se consideran mas adelante, deberan evaluarse por separado.

---

# Stack tecnologico

| Elemento | Estado actual | Propuesta futura |
|---|---|---|
| Backend logging | Spring Boot / Logback por defecto | Politica consistente de mensajes y niveles |
| Destino | `stdout` | `stdout` |
| Correlacion | No confirmada en el proyecto actual | Correlation ID por peticion, si se valida |

---

# Diagrama del stack

```
Peticion HTTP
   |
   v
Backend
   |
   +-> logs con formato consistente
   +-> sin credenciales ni secretos
   +-> correlation ID si se implementa
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Mantener logging sin lineamientos | Mantiene inconsistencia y menor trazabilidad |
| Definir desde ya una plataforma externa de observabilidad | No esta confirmada en el proyecto actual |
| No considerar correlation ID | Reduce capacidad futura de seguimiento por peticion |

---

# Beneficios Arquitectonicos

* Mejora mantenimiento y diagnostico
* Reduce riesgo de exponer informacion sensible en logs
* Permite una evolucion futura ordenada si luego se adoptan herramientas externas

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Lineamientos simples y pragmaticos | Requiere disciplina al escribir logs y revisar mensajes existentes |
| Correlation ID puede mejorar trazabilidad | Introduce un componente transversal si se implementa |

---

# Impacto en el Sistema

**Backend:** Debe normalizar la forma de registrar eventos y errores.

**Seguridad:** Debe prohibir expresamente el registro de credenciales y secretos.

**Operaciones:** Mantiene `stdout` como salida base y evita asumir plataformas externas no confirmadas.

---

# Evidencia

* `backend-unab-master/pom.xml`
* `backend-unab-master/src/main/resources/application.properties`
* `docker-compose.yml`

---

# Validacion

* Confirmar que el proyecto actual no implementa correlation ID
* Confirmar que no existe una politica documentada de logging mas alla del comportamiento por defecto
* Requiere validacion antes de implementacion sobre el formato exacto y el alcance del correlation ID
