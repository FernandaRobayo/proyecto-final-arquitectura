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
Respuesta    -> JSON con accessToken de tipo Basic
Roles seed   -> ROLE_ADMIN
               ROLE_STAFF
Frontend     -> auth.interceptor.ts adjunta Authorization
```

No se confirma en el codigo actual una autorizacion fina por rol en endpoints; la configuracion observada exige autenticacion para `anyRequest()`.

---

# Diagrama del flujo de autenticacion

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

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Implementacion simple y coherente con Spring Security | HTTP Basic requiere proteger el transporte en despliegues productivos |
| Compatible con SPA y API separadas | No se observa revocacion ni expiracion de token mas alla de la credencial base |

---

# Impacto en el Sistema

**Backend:** `SpringSecurityConfig`, `AuthRestController` y `UsuarioService`.

**Frontend:** `LoginService` consume `/api/auth/login` y `AuthInterceptor` reenvia el token.

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
