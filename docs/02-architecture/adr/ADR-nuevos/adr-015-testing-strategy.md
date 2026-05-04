# ADR-015 — Estrategia de pruebas automatizadas por capas

## Estado

Propuesto — 2026-05-02

---

# Contexto

El proyecto incluye la infraestructura de testing por defecto de Spring Boot (`spring-boot-starter-test` con JUnit 5 y Mockito) y Angular (Karma + Jasmine con Protractor para E2E), pero no existe una estrategia documentada que defina qué se prueba, cómo se aísla y qué cobertura es aceptable en cada capa.

Sin esta estrategia, los desarrolladores toman decisiones individuales sobre nivel de aislamiento, cobertura y herramientas, generando inconsistencias en la calidad del conjunto de pruebas y en los criterios de aceptación del pipeline de CI/CD.

---

# Problema

Sin una estrategia de pruebas definida:

* no se sabe qué cobertura de pruebas existe ni cuál es el umbral aceptable
* las pruebas de integración pueden levantar el contexto completo de Spring cuando solo necesitan probar una unidad
* las pruebas de base de datos pueden usar la misma instancia de producción si no están aisladas
* el pipeline de CI/CD no tiene un criterio claro de cuándo una build es válida en términos de pruebas
* los tests E2E (Protractor) pueden estar desactualizados sin saberlo

---

# Decisión Arquitectónica

Se propone adoptar una **estrategia de pruebas en pirámide** con tres niveles de aislamiento:

```
                  ┌─────────────────────────────┐
                  │      E2E / Angular          │
                  │   Protractor — flujos clave │  Pocos, lentos, alto valor
                  └─────────────┬───────────────┘
                                │
                  ┌─────────────▼───────────────┐
                  │   Integración / API         │
                  │  @SpringBootTest + H2        │  Moderados
                  │  o Testcontainers MySQL      │
                  └─────────────┬───────────────┘
                                │
                  ┌─────────────▼───────────────┐
                  │       Unitarias             │
                  │  JUnit 5 + Mockito (back)   │  Muchas, rápidas
                  │  Karma + Jasmine (front)    │
                  └─────────────────────────────┘
```

Reglas de la estrategia:

```
Unitarias      → Una clase · Dependencias mockeadas · Rápidas
                 Objetivo: cubrir lógica de negocio en Services

Integración    → Controller → Service → DAO · H2 en memoria
                 NO usan la base de datos de producción
                 Verifican el flujo completo de una petición

E2E            → Escenarios críticos del usuario: login, CRUD principal
                 Se ejecutan en entorno similar a producción
                 No más de 10-15 escenarios clave

Umbral         → Cobertura mínima: 70% en capa de Services (backend)
                 Bloquea el pipeline de CI/CD si no se alcanza
```

---

# Diagrama de la pirámide de pruebas

```
                  ╔═════════════════════════════╗
                  ║      E2E (Protractor)       ║
                  ║  login → CRUD → logout      ║   3-10 tests
                  ╠═════════════════════════════╣
                  ║   Integración (@SpringBoot) ║
                  ║  HTTP → Service → H2/MySQL  ║  10-30 tests
                  ╠═════════════════════════════╣
                  ║       Unitarias             ║
                  ║  Service + lógica negocio   ║  50+ tests
                  ║  Karma + Jasmine (Angular)  ║
                  ╚═════════════════════════════╝

  Velocidad:     Rápido ─────────────────────▶ Lento
  Confianza:     Baja ──────────────────────▶ Alta
  Cantidad:      Muchos ──────────────────▶ Pocos
```

---

# Alternativas consideradas

| Alternativa | Descripción | Por qué no se eligió |
|---|---|---|
| Solo pruebas unitarias | Máxima velocidad, mínima confianza en integración | No detecta problemas de integración entre capas ni de configuración de Spring |
| Solo pruebas de integración con BD real | Mayor fidelidad, máxima lentitud | Lentas, requieren BD disponible, no aisladas entre ejecuciones |
| Sin estrategia formal | Cada desarrollador decide | Inconsistencia en cobertura; sin criterio de aceptación para CI/CD |

---

# Beneficios Arquitectónicos

* La pirámide garantiza velocidad (muchas unitarias) con confianza en integración (pocas de integración)
* H2 en memoria o Testcontainers aíslan las pruebas de integración de la base de datos de producción
* Un criterio de cobertura mínimo documentado permite bloquear builds insuficientes en CI/CD
* La estrategia es incremental: puede implementarse por módulo empezando por los Services

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Pirámide equilibra velocidad y confianza | Requiere tiempo inicial para escribir pruebas de código existente |
| H2/Testcontainers aíslan pruebas de integración | H2 no es 100% compatible con MySQL; puede no detectar problemas específicos de MySQL 8.0 |
| Criterio de cobertura objetivo en CI/CD | Testcontainers requiere Docker disponible en el agente de CI/CD |

---

# Impacto en el Sistema

**Backend:** Verificar cobertura actual con Jacoco; agregar H2 o Testcontainers para integración; documentar umbral de cobertura mínima (70% en Services).

**Frontend:** Verificar estado de pruebas Karma/Jasmine; definir alcance de tests Protractor E2E; documentar escenarios críticos cubiertos.

**Base de datos:** Agregar H2 como dependencia de test para aislar pruebas de integración.

**DevOps:** El pipeline de CI/CD debe ejecutar los tres niveles y bloquear si la cobertura cae por debajo del umbral definido.

**Seguridad:** Las pruebas de integración no deben usar credenciales de producción; usar datos de prueba aislados.

**Documentación:** El umbral de cobertura y los comandos para ejecutar cada nivel deben documentarse en `README.md`.

---

# Evidencia (estado actual — base para la propuesta)

* `backend-unab-master/pom.xml`: Incluye `spring-boot-starter-test` (JUnit 5 + Mockito). No se evidencia Testcontainers ni Jacoco
* `frontend-unab-master/karma.conf.js`: Configuración Karma para pruebas unitarias Angular. Contenido no está en las entradas
* `frontend-unab-master/e2e/`: Directorio de pruebas E2E con Protractor. Contenido no está en las entradas

---

# Validación

* Confirmar que existen pruebas unitarias para la capa de Services del backend con Mockito
* Verificar que las pruebas de integración usan H2 en memoria o Testcontainers (no la BD de producción)
* Confirmar que `ng test` ejecuta pruebas unitarias de Angular sin errores
* Verificar que el reporte de cobertura Jacoco muestra ≥70% en la capa de Services
* Confirmar que el pipeline de CI/CD bloquea builds por debajo del umbral de cobertura
