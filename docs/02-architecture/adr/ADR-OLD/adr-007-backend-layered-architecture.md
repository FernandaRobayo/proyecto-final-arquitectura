# ADR-007 - Arquitectura en capas del backend con contratos DTO

## Estado

Aceptado - 2026-05-02

---

# Contexto

El backend esta organizado para separar recepcion HTTP, logica de negocio, acceso a datos, modelo de dominio y contratos expuestos por la API.

---

# Decision Arquitectonica

El backend sigue la siguiente organizacion:

```
controllers/      -> Controladores REST
models/services/  -> Interfaces e implementaciones de servicios
models/dao/       -> Repositorios Spring Data JPA
models/entity/    -> Entidades JPA
dto/              -> Contratos de entrada y salida
exception/        -> Manejo global de errores
auth/             -> Configuracion de seguridad
```

Los controladores retornan DTOs y delegan la logica a servicios; los servicios interactuan con DAOs y hacen mapeo manual entre entidades y DTOs.

---

# Diagrama de capas

```
HTTP -> Controller -> Service -> DAO -> Entity
          |
          v
          DTO

Errores comunes -> GlobalExceptionHandler
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| Exponer entidades directamente | Acoplaria el contrato REST al modelo de persistencia |
| Arquitectura hexagonal completa | No hay evidencia de esa estructura en el proyecto actual |
| Organizacion puramente por feature | La implementacion actual esta organizada por capas tecnicas |

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Mejora separacion de responsabilidades | Introduce mas clases por recurso |
| DTOs desacoplan API y persistencia | El mapeo es manual y requiere mantenimiento |

---

# Evidencia

* `backend-unab-master/src/main/java/com/backend/unab/controllers/`
* `backend-unab-master/src/main/java/com/backend/unab/models/services/`
* `backend-unab-master/src/main/java/com/backend/unab/models/dao/`
* `backend-unab-master/src/main/java/com/backend/unab/models/entity/`
* `backend-unab-master/src/main/java/com/backend/unab/dto/`
* `backend-unab-master/src/main/java/com/backend/unab/exception/GlobalExceptionHandler.java`

---

# Validacion

* Confirmar estructura de paquetes del backend
* Confirmar uso de DTOs en controladores
* Confirmar `GlobalExceptionHandler` para respuestas de error
