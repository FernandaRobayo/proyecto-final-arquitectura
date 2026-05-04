# ADR-017 — Gestión segura de secretos y variables de entorno

## Estado

Propuesto — 2026-05-02

---

# Contexto

El sistema usa variables de entorno para gestionar credenciales y configuración sensible: contraseñas de MySQL, URL de conexión a base de datos y el parámetro `SPRING_JPA_HIBERNATE_DDL_AUTO`. Docker Compose carga estas variables desde un archivo `.env` en la raíz del proyecto.

No existe una política documentada sobre qué va en `.env`, cómo se gestiona ese archivo en distintos entornos ni cómo se previene que secretos reales lleguen al repositorio de código.

---

# Problema

Sin una política de gestión de secretos:

* el archivo `.env` con credenciales reales puede ser accidentalmente incluido en commits si no está en `.gitignore`
* no existe distinción documentada entre variables de desarrollo (valores de prueba) y producción (credenciales reales)
* no hay un mecanismo para auditar quién tiene acceso a las credenciales de producción
* los secretos en texto plano en `.env` no están cifrados en reposo

Riesgo concreto:

```bash
# Si .env contiene credenciales reales y llega al repositorio:
MYSQL_ROOT_PASSWORD=contraseña-real-de-produccion

# → Compromiso total de la base de datos
# → El secreto queda en el historial de git para siempre
```

---

# Decisión Arquitectónica

Se propone adoptar la siguiente política de gestión de secretos por entorno:

```
Desarrollo local
  · .env con valores de PRUEBA (no reales)
  · .env SIEMPRE en .gitignore
  · .env.example en el repositorio con placeholders
    Ejemplo: MYSQL_ROOT_PASSWORD=change_me_here

Staging / CI-CD
  · Variables inyectadas por el sistema de CI/CD
    (GitHub Actions secrets, GitLab CI variables)
  · Sin archivos .env en el servidor o agente

Producción
  · Docker Secrets o gestor externo
    (AWS Secrets Manager, HashiCorp Vault)
  · Sin .env como archivo en el servidor de producción
```

**Regla fundamental: ningún secreto real llega al repositorio.**

---

# Diagrama de la política por entorno

```
  Repositorio Git
  ┌─────────────────────────────────────────────┐
  │  .env.example   ← plantilla con placeholders│
  │  .gitignore     ← contiene .env             │
  │  docker-compose.yml ← lee ${VAR}            │
  └──────────────────┬──────────────────────────┘
                     │
          ┌──────────▼──────────┐    ┌──────────────────────┐
          │   Desarrollo local  │    │   CI/CD / Producción │
          │                     │    │                      │
          │   .env              │    │   Variables del      │
          │   (ignorado por git)│    │   sistema CI/CD      │
          │   valores de prueba │    │   o Docker Secrets   │
          └─────────────────────┘    │   (no en disco       │
                                     │    como archivo)     │
                                     └──────────────────────┘
```

---

# Alternativas consideradas

| Alternativa | Descripción | Por qué no se eligió |
|---|---|---|
| Secretos hardcodeados en `application.properties` | Valores directamente en el código | Máximo riesgo: los secretos quedan en el historial de git de forma permanente |
| Solo `.gitignore` sin política documentada | El archivo `.env` existe pero sin política explícita | Dependiente del conocimiento individual; cualquier colaborador nuevo puede cometer el error |
| Vault / AWS Secrets Manager desde el inicio | Gestor de secretos centralizado y cifrado | Correcto para producción avanzada; la propuesta lo incluye como destino final; el paso inicial es documentar la política base |

---

# Beneficios Arquitectónicos

* Garantiza que ningún secreto real llega al repositorio de código fuente
* `.env.example` documenta qué variables son necesarias para nuevos desarrolladores
* La separación por entorno alinea el sistema con buenas prácticas de seguridad (dev / staging / prod)
* La política es incremental: comienza con `.gitignore` + `.env.example` y evoluciona a Docker Secrets o Vault

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Previene exposición accidental de credenciales | Cada desarrollador debe configurar su propio `.env` desde `.env.example` |
| `.env.example` documenta las variables necesarias | En CI/CD, alguien debe gestionar y rotar los secretos en el sistema de pipelines |
| Evolución incremental hacia gestores de secretos | Docker Secrets o Vault agregan complejidad operacional en la etapa avanzada |
| Política auditable y documentada en el repositorio | — |

---

# Impacto en el Sistema

**Backend:** Sin cambios de código. Spring Boot ya lee variables de entorno; el cambio es en cómo se proveen en cada entorno.

**Frontend:** Sin impacto directo.

**Base de datos:** Sin cambios. MySQL ya lee credenciales por variable de entorno en `docker-compose.yml`.

**DevOps:** Crear `.env.example` en la raíz del proyecto; confirmar que `.env` está en `.gitignore`; documentar el proceso de configuración en `README-DOCKER.md`.

**Seguridad:** Reduce drásticamente el riesgo de exposición de credenciales en el repositorio. La adopción de Docker Secrets o Vault en producción es el siguiente paso de madurez.

**Documentación:** `README-DOCKER.md` debe incluir el paso de copiar `.env.example` a `.env` y completar los valores antes de ejecutar `docker compose up`.

---

# Evidencia (estado actual — base para la propuesta)

* `docker-compose.yml`: Variables `MYSQL_ROOT_PASSWORD`, `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD` leídas desde el entorno; implica uso de `.env`
* `README-DOCKER.md`: Menciona el archivo `.env` como paso de configuración
* No se confirmó si `.env` está en `.gitignore`. No está en las entradas
* No se confirmó la existencia de `.env.example` en el repositorio. No está en las entradas

---

# Validación

* Confirmar que `.env` está listado en `.gitignore` raíz del proyecto
* Verificar que `.env.example` existe con todos los nombres de variables y valores de placeholder
* Confirmar que ningún secreto real está hardcodeado en `docker-compose.yml`, `application.properties` ni en el repositorio
* Verificar que el proceso de configuración para nuevos desarrolladores está documentado en `README-DOCKER.md`
* Para producción: confirmar que las credenciales no se pasan como archivo `.env` sino por el mecanismo del sistema de despliegue
