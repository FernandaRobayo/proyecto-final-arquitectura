# ADR-009 - Modelo de documentacion arquitectonica C4 con vistas as-is y to-be

## Estado

Aceptado - 2026-05-02

---

# Contexto

El proyecto ya organiza su documentacion de arquitectura bajo `docs/02-architecture/`, separando vistas del estado actual y vistas objetivo.

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

Las vistas `as-is` documentan la implementacion actual confirmada y las `to-be` documentan el estado objetivo planificado.

---

# Diagrama del modelo

```
as-is -> arquitectura implementada
to-be -> arquitectura objetivo
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

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Facilita lectura por niveles | Requiere mantener sincronizadas las vistas con el codigo |
| Distingue estado actual y objetivo | La cobertura de niveles no es identica entre `as-is` y `to-be` |

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
* Confirmar vistas C4 reales presentes en cada carpeta
* Confirmar que `adr/` existe como area de decisiones arquitectonicas
