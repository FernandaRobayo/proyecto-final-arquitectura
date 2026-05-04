# ADR-006 - Comunicacion REST sobre HTTP/JSON entre frontend y backend

## Estado

Aceptado - 2026-05-02

---

# Contexto

El frontend y el backend se ejecutan como aplicaciones separadas y necesitan un contrato de integracion sencillo y consistente.

---

# Decision Arquitectonica

La comunicacion actual usa:

```
Protocolo      -> HTTP
Estilo         -> API REST
Formato        -> JSON
Prefijo rutas  -> /api/*
Resolucion URL -> api-url.ts / resolveApiBaseUrl()
Cliente base   -> BaseResourceService para CRUD generico
```

---

# Diagrama

```
Angular SPA
   |
   | HTTP/JSON
   v
Spring Boot API
   |
   +-- /api/auth/login
   +-- /api/customers
   +-- /api/species
   +-- /api/breeds
   +-- /api/pets
   +-- /api/veterinarians
   +-- /api/appointments
   +-- /api/medical-records
   +-- /api/treatments
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| GraphQL | No hay evidencia de su uso en el proyecto actual |
| gRPC | No corresponde con el consumo navegador + Angular observado |
| BFF adicional | No hay un servicio intermedio implementado |

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Integracion simple y ampliamente soportada | No hay versionado explicito de la API en las rutas actuales |
| Reutilizacion de servicios base en Angular | La documentacion formal de la API no esta implementada |

---

# Evidencia

* `frontend-unab-master/src/app/utils/api-url.ts`
* `frontend-unab-master/src/app/services/base-resource.service.ts`
* `frontend-unab-master/src/app/views/login/login.service.ts`
* `backend-unab-master/src/main/java/com/backend/unab/controllers/CustomerRestController.java`
* `backend-unab-master/src/main/java/com/backend/unab/controllers/AppointmentRestController.java`

---

# Validacion

* Confirmar `resolveApiBaseUrl()` en `api-url.ts`
* Confirmar consumo de `/api/auth/login` desde Angular
* Confirmar endpoints REST bajo `/api/*` en controladores backend
