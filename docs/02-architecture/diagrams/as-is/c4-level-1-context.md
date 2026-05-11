# Nivel 1 - Contexto

## Descripcion

Este diagrama muestra el contexto del sistema actual a nivel C4 Level 1. Representa a las personas usuarias internas confirmadas y al sistema principal, sin descomponerlo en frontend, backend o base de datos.

## Diagrama

- [c4-level-1-context.png](C:/www/proyecto-final-arquitectura/docs/02-architecture/diagrams/as-is/c4-level-1-context.png)

## Elementos

- `Administrador`
- `Personal operativo de la veterinaria`
- `Sistema Veterinario`

## Relaciones

- `Administrador -> Sistema Veterinario`
  Usa la aplicacion web desde un navegador para autenticarse y gestionar la administracion general del sistema.
- `Personal operativo de la veterinaria -> Sistema Veterinario`
  Usa la aplicacion web desde un navegador para apoyar la gestion operativa de clientes, especies, razas, mascotas, veterinarios, citas, historias clinicas y tratamientos.

## Observaciones

- Este nivel no incluye frontend, backend ni base de datos, porque eso pertenece al nivel 2.
- No se modelan sistemas externos adicionales porque no se confirmaron integraciones externas en el proyecto actual.
