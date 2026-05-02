# Nivel 1 - Contexto

## Descripcion

Este diagrama muestra el contexto del sistema actual a nivel C4 Level 1. Representa unicamente a la persona usuaria y al sistema principal, sin descomponerlo en frontend, backend o base de datos.

## Diagrama

- [c4-level-1-context.puml](C:/www/proyecto-final-arquitectura/docs/02-architecture/diagrams/as-is/c4-level-1-context.puml)

## Elementos

- `Usuario web`
- `Sistema Veterinario`

## Relaciones

- `Usuario web -> Sistema Veterinario`
  Usa la aplicacion web desde un navegador para autenticarse y gestionar clientes, especies, razas, mascotas, veterinarios, citas, historias clinicas y tratamientos.

## Observaciones

- Este nivel no incluye frontend, backend ni base de datos, porque eso pertenece al nivel 2.
- No se modelan sistemas externos adicionales porque no se confirmaron integraciones externas en el proyecto actual.
