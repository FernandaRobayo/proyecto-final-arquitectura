# ADR-005 - Autenticacion stateless con Spring Security y HTTP Basic

## Estado

Aceptado - 2026-05-02

---

# Contexto

El backend expone una API REST protegida y el frontend es una SPA separada. El proyecto implementa autenticacion con usuarios almacenados en base de datos y dos roles tecnicos definidos en el seed: `ROLE_ADMIN` y `ROLE_STAFF`.

---

# Problema

Sin autenticacion definida:

* cualquier cliente podria consumir la API sin control
* el frontend no tendria una forma uniforme de reenviar credenciales en peticiones posteriores
* no existiria integracion clara entre usuarios, roles y acceso autenticado

---

# Decision Arquitectonica

La autenticacion actual usa:

```
Mecanismo    -> HTTP Basic Authentication
Sesiones     -> Stateless (SessionCreationPolicy.STATELESS)
Login        -> POST /api/auth/login
Respuesta    -> JSON con credencial Basic reconstruida para reutilizacion
Roles seed   -> ROLE_ADMIN
               ROLE_STAFF
Frontend     -> auth.interceptor.ts adjunta Authorization
```

El `accessToken` actual no corresponde a un token emitido por el servidor con semantica de JWT u otro mecanismo independiente. La implementacion observada devuelve una credencial `Basic ...` construida a partir de `username:password` y utilizada por el frontend para reenviar el encabezado `Authorization`.

No se confirma en el codigo actual una autorizacion fina por rol en endpoints; la configuracion observada exige autenticacion para `anyRequest()`.

---

# Stack tecnologico

| Elemento | Tecnologia o mecanismo | Evidencia principal |
|---|---|---|
| Seguridad backend | Spring Security | `SpringSecurityConfig.java` |
| Transporte de credenciales | HTTP Basic | `SpringSecurityConfig.java`, `AuthRestController.java` |
| Password hashing | BCrypt | `SpringSecurityConfig.java`, `auth-seed.sql` |
| Persistencia usuarios y roles | MySQL + Spring Data JPA | `UsuarioService.java`, `auth-seed.sql` |

---

# Diagrama del stack

```
Angular SPA -> POST /api/auth/login -> AuthRestController
                |
                v
        AuthenticationManager / Spring Security
                |
                v
            UsuarioService -> users / roles

Luego:
Frontend adjunta Authorization: Basic ... en llamadas a /api/*
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| JWT | No esta implementado en el proyecto actual |
| OAuth2 / OIDC | No hay evidencia de infraestructura externa para soportarlo |
| Sesiones stateful | No corresponde con la configuracion `STATELESS` observada |

---

# Beneficios Arquitectonicos

* Implementacion simple y coherente con Spring Security
* Integracion directa con usuarios y roles persistidos en base de datos
* Menor complejidad que mecanismos tokenizados mas avanzados

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Implementacion simple y coherente con Spring Security | HTTP Basic requiere proteger el transporte en despliegues reales |
| Compatible con SPA y API separadas | La respuesta de login puede confundirse con un token emitido por el servidor si no se documenta correctamente |
| Persistencia de roles en base de datos | No se observa revocacion ni expiracion independiente de la credencial |

---

# Impacto en el Sistema

**Backend:** `SpringSecurityConfig`, `AuthRestController` y `UsuarioService`.

**Frontend:** `LoginService` consume `/api/auth/login` y `AuthInterceptor` reenvia la credencial `Authorization`.

**Base de datos:** Usuarios y roles iniciales definidos en `auth-seed.sql`.

**Seguridad:** Todos los endpoints salvo el login requieren autenticacion, segun la configuracion actual.

---

# Evidencia

* `backend-unab-master/src/main/java/com/backend/unab/auth/SpringSecurityConfig.java`
* `backend-unab-master/src/main/java/com/backend/unab/controllers/AuthRestController.java`
* `backend-unab-master/src/main/java/com/backend/unab/models/services/UsuarioService.java`
* `backend-unab-master/src/main/resources/auth-seed.sql`
* `frontend-unab-master/src/app/interceptors/auth.interceptor.ts`

---

# Validacion

* Confirmar `SessionCreationPolicy.STATELESS` y `httpBasic()` en `SpringSecurityConfig.java`
* Confirmar `POST /api/auth/login` en `AuthRestController.java`
* Confirmar `ROLE_ADMIN` y `ROLE_STAFF` en `auth-seed.sql`
* Confirmar que la respuesta de login devuelve una credencial `Basic ...` y no un JWT
