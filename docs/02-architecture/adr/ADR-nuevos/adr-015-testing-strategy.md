# ADR-015 - Estrategia incremental de pruebas automatizadas por capas

## Estado

Requiere validacion - 2026-05-02

---

# Contexto

El proyecto ya cuenta con evidencia de pruebas automatizadas, pero de forma parcial y sin una estrategia consolidada documentada:

* backend con pruebas unitarias sobre servicios
* frontend con configuracion de pruebas unitarias Angular
* frontend con estructura E2E de Protractor

No se confirma en el proyecto actual una politica formal de cobertura ni un pipeline CI/CD operativo.

---

# Problema

Sin una estrategia documentada de pruebas:

* no hay una expectativa comun sobre que capas deben cubrirse
* la calidad depende de decisiones individuales del equipo
* es dificil priorizar que tipos de pruebas agregar primero

---

# Decision Arquitectonica

Requiere validacion antes de implementacion.

Se propone adoptar una estrategia incremental de pruebas por capas, priorizando:

* pruebas unitarias de servicios backend
* pruebas unitarias de componentes y guards frontend
* validaciones de integracion basicas sobre endpoints criticos
* mantenimiento o depuracion de los flujos E2E realmente utiles

No se aprueban en este ADR herramientas o umbrales especificos no confirmados en el proyecto actual.

---

# Stack tecnologico

| Elemento | Estado actual | Propuesta futura |
|---|---|---|
| Backend tests | JUnit 5 + Mockito via `spring-boot-starter-test` | Continuar y ampliar |
| Frontend unit tests | Karma + Jasmine | Continuar y ampliar |
| Frontend E2E | Protractor presente en el proyecto | Revisar utilidad real y mantener solo escenarios valiosos |

---

# Diagrama del stack

```
Backend services -> pruebas unitarias
Frontend components/guards -> pruebas unitarias
Endpoints criticos -> validacion de integracion basica
Flujos clave usuario -> E2E selectivo
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| No documentar estrategia | Mantiene dispersion y criterios implícitos |
| Apostar solo por E2E | Aumenta costo y fragilidad para este proyecto |
| Fijar desde ya herramientas y umbrales no presentes | No esta respaldado por evidencia actual |

---

# Beneficios Arquitectonicos

* Ordena el crecimiento de pruebas sin sobredimensionar la solucion
* Alinea pruebas con la arquitectura por capas del proyecto
* Facilita discutir prioridades tecnicas antes de invertir en tooling adicional

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Estrategia pragmatica y compatible con el estado real del repositorio | Requiere acuerdo posterior del equipo para concretar alcance y criterios |
| Aprovecha lo ya existente | No resuelve por si sola cobertura o automatizacion futura |

---

# Impacto en el Sistema

**Backend:** Consolidar y ampliar pruebas sobre servicios y comportamiento critico.

**Frontend:** Mantener pruebas utiles y revisar el valor real de los E2E existentes.

**Documentacion:** Debe dejar claro que hoy la estrategia es una propuesta incremental y no una politica plenamente implementada.

---

# Evidencia

* `backend-unab-master/pom.xml`
* `backend-unab-master/src/test/java/com/backend/unab/models/services/`
* `frontend-unab-master/karma.conf.js`
* `frontend-unab-master/e2e/`
* `frontend-unab-master/src/app/**/*.spec.ts`

---

# Validacion

* Confirmar que siguen existiendo pruebas unitarias backend en `src/test`
* Confirmar que el frontend mantiene configuracion de Karma/Jasmine y estructura E2E
* Requiere validacion antes de implementacion sobre alcance, prioridad y mantenimiento de los E2E
