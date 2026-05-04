# ADR-008 - Patron CRUD generico dirigido por metadata en el frontend

## Estado

Aceptado - 2026-05-02

---

# Contexto

El frontend maneja multiples recursos del dominio veterinario con operaciones CRUD similares. La implementacion actual reutiliza componentes y configuracion por recurso en lugar de construir una pantalla totalmente distinta para cada modulo.

---

# Decision Arquitectonica

El frontend usa un patron generico basado en:

```
ResourceRegistryService
    -> Define configuracion por recurso
       rutas, columnas, campos, opciones y servicio

ResourceIndexComponent
    -> Listado generico

ResourceFormComponent
    -> Formulario generico

BaseResourceService
    -> Operaciones CRUD comunes contra /api/*
```

---

# Diagrama

```
app-routing.module.ts
        |
        v
ResourceIndexComponent / ResourceFormComponent
        |
        v
ResourceRegistryService
        |
        v
Servicios por recurso -> BaseResourceService -> API REST
```

---

# Alternativas consideradas

| Alternativa | Por que no se eligio |
|---|---|
| CRUD especifico por entidad | Aumentaria duplicacion de vistas y logica repetitiva |
| Libreria externa de formularios dinamicos | No hay evidencia de su uso en el proyecto actual |
| Microfrontends | No corresponde con el tamano ni la estructura actual del proyecto |

---

# Trade-offs

| Ventaja | Desventaja |
|---|---|
| Reduce duplicacion en pantallas CRUD | Centraliza mucha configuracion en `ResourceRegistryService` |
| Facilita agregar nuevos recursos similares | Algunos textos y etiquetas quedan concentrados en metadata |

---

# Evidencia

* `frontend-unab-master/src/app/views/resources/resource-index/resource-index.component.ts`
* `frontend-unab-master/src/app/views/resources/resource-form/resource-form.component.ts`
* `frontend-unab-master/src/app/services/base-resource.service.ts`
* `frontend-unab-master/src/app/services/resource-registry.service.ts`
* `frontend-unab-master/src/app/app-routing.module.ts`

---

# Validacion

* Confirmar que varias rutas usan `ResourceIndexComponent` y `ResourceFormComponent`
* Confirmar registro por recurso en `ResourceRegistryService`
* Confirmar CRUD comun en `BaseResourceService`
