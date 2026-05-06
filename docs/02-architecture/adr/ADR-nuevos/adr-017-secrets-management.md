# ADR-017 - Gestion segura de secretos y variables de entorno

## Estado

Propuesto - 2026-05-02

---

# Contexto

El proyecto usa variables de entorno para base de datos y configuracion sensible en `docker-compose.yml`. Tambien existe un archivo `.env` en la raiz del proyecto con valores concretos y ese archivo esta versionado en git.

No se confirma en la raiz del proyecto la existencia de `.gitignore` ni de un `.env.example`.

---

# Problema

Sin una politica minima de secretos:

* los valores de `.env` pueden quedar expuestos en el repositorio
* no hay separacion clara entre configuracion local de ejemplo y secretos reales
* el onboarding tecnico carece de una plantilla segura de variables requeridas

---

# Decision Arquitectonica

Propuesta futura.

Se propone adoptar una politica minima e inmediata de gestion de secretos:

**Minimo obligatorio inmediato**

* dejar de tratar `.env` como archivo versionable con valores operativos
* crear `.env.example` con placeholders
* definir que los valores reales deben inyectarse por ambiente y no conservarse en el repositorio

**Evolucion futura opcional**

* evaluar mecanismos mas robustos de provision de secretos segun el entorno de despliegue real

No se aprueba en este ADR una herramienta externa especifica de secretos porque no esta implementada ni confirmada en el proyecto actual.

---

# Stack tecnologico

| Elemento | Estado actual | Propuesta futura |
|---|---|---|
| Variables sensibles | `.env` en raiz + variables de entorno en Docker Compose | Plantilla `.env.example` + valores reales fuera del repositorio |
| Orquestacion local | Docker Compose | Docker Compose |
| Gestor externo de secretos | No confirmado en el proyecto actual | Requiere validacion antes de implementacion |

---

# Diagrama del stack

```
Repositorio
   |
   +-> .env.example con placeholders
   |
Entorno local o real
   |
   +-> valores reales inyectados fuera del repositorio
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Mantener `.env` versionado | Incrementa riesgo de exposicion de secretos |
| No documentar ninguna politica | Mantiene ambiguedad operativa y de seguridad |
| Aprobar desde ya un gestor externo concreto | No esta implementado ni confirmado en el proyecto actual |

---

# Beneficios Arquitectonicos

* Reduce riesgo de exponer credenciales en el repositorio
* Mejora separacion entre configuracion de ejemplo y configuracion real
* Hace mas claro el manejo por ambiente

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Politica inmediata y simple | Requiere cambiar habitos del equipo y del repositorio |
| `.env.example` mejora onboarding | Obliga a mantener sincronizada la lista de variables necesarias |

---

# Impacto en el Sistema

**DevOps:** Debe separar configuracion local de ejemplo y valores reales por ambiente.

**Seguridad:** Reduce exposicion accidental de credenciales.

**Documentacion:** Debe explicar el uso de `.env.example` y evitar presentar `.env` versionado como practica aceptable.

---

# Evidencia

* `docker-compose.yml`
* `.env`
* `README-DOCKER.md`
* `git ls-files .env`

---

# Validacion

* Confirmar que `.env` existe en la raiz y esta versionado
* Confirmar que no existe `.env.example` en la raiz del proyecto
* Confirmar que no existe `.gitignore` en la raiz del proyecto
* Requiere validacion antes de implementacion sobre el mecanismo futuro por ambiente fuera del repositorio
