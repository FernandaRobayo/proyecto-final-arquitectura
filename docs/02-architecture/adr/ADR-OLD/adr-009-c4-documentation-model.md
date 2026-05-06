# ADR-009 - Modelo de documentacion arquitectonica C4 con vistas as-is y to-be

## Estado

Aceptado - 2026-05-02

---

# Contexto

El proyecto organiza su documentacion de arquitectura bajo `docs/02-architecture/`, separando vistas del estado actual y vistas objetivo.

---

# Problema

Sin un modelo comun de documentacion arquitectonica:

* se dificulta explicar la arquitectura por niveles
* la relacion entre estado actual y evolucion futura queda ambigua
* las decisiones documentadas en ADR pierden contexto visual

---

# Decision Arquitectonica

La documentacion arquitectonica usa el modelo C4 con esta organizacion:

```
docs/02-architecture/
├── diagrams/
│   ├── as-is/
│   │   ├── c4-level-1-context.md
│   │   ├── c4-level-2-containers.md
│   │   ├── c4-level-3-components.md
│   │   └── erd-as-is.md
│   └── to-be/
│       ├── c4-level-1-context.md
│       ├── c4-level-2-containers.md
│       ├── c4-level-3-components.md
│       ├── c4-level-4-code.md
│       └── erd-to-be.md
└── adr/
```

Las vistas `as-is` documentan la implementacion actual confirmada. Las vistas `to-be` existen como espacio reservado para el estado objetivo, pero su contenido puede estar pendiente o incompleto en el proyecto actual.

---

# Stack tecnologico

No aplica directamente para este ADR.

---

# Diagrama del stack

```
as-is -> arquitectura implementada
to-be -> arquitectura objetivo o pendiente de completar
ADR   -> decisiones y razonamiento
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Diagramas ad hoc sin modelo comun | Reducen consistencia entre vistas |
| Solo documentacion textual | Hace mas dificil comunicar arquitectura a distintos perfiles |
| UML completo para todo | No hay evidencia de ese enfoque en el proyecto actual |

---

# Beneficios Arquitectonicos

* Facilita lectura por niveles
* Separa estado actual de evolucion futura
* Da contexto visual a las decisiones documentadas en ADR

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Facilita lectura por niveles | Requiere mantener sincronizadas las vistas con el codigo |
| Distingue estado actual y objetivo | El area `to-be` aun no esta completa en el proyecto actual |

---

# Impacto en el Sistema

**Documentacion actual:** `as-is` contiene vistas utiles para contexto, contenedores y componentes.

**Documentacion futura:** `to-be` no debe presentarse como completa mientras sus archivos sigan vacios o incompletos.

**ADR:** Los ADR actuales y futuros deben apoyarse en `as-is` cuando describen implementacion real y en `to-be` solo cuando exista contenido suficiente.

---

# Evidencia

* `docs/02-architecture/diagrams/as-is/c4-level-1-context.md`
* `docs/02-architecture/diagrams/as-is/c4-level-2-containers.md`
* `docs/02-architecture/diagrams/as-is/c4-level-3-components.md`
* `docs/02-architecture/diagrams/to-be/`
* `docs/02-architecture/adr/`

---

# Validacion

* Confirmar las carpetas `as-is` y `to-be`
* Confirmar que `as-is` contiene vistas reales presentes en el repositorio
* Confirmar que `to-be` existe, pero sin afirmar cobertura completa si sus archivos siguen vacios o incompletos
