# HU-06 - Gestión de razas

## Historia de Usuario

Como personal operativo de la veterinaria,  
quiero registrar y actualizar razas,  
para clasificar correctamente las mascotas según su especie.

## Descripción funcional

El sistema debe permitir consultar, registrar, actualizar y eliminar razas, asociándolas a una especie existente.

## Módulo

Razas.

## Capa

Fullstack.

## Prioridad

Media.

## Estimación

5 puntos Scrum.

## Definición de Ready (DoR)

- [ ] La necesidad de administrar razas asociadas a especies está claramente definida.
- [ ] Se encuentran identificados los datos obligatorios para registrar una raza.
- [ ] Está identificada la dependencia funcional con especies previamente registradas.
- [ ] Se encuentran definidas las operaciones de consulta, registro, edición y eliminación.
- [ ] Los comportamientos no confirmados quedaron marcados como `Requiere validación` o registrados como riesgo.

## Definición de Done (DoD)

- [ ] La historia documenta criterios visibles para consultar, crear, editar y eliminar razas.
- [ ] El sistema valida el nombre y la especie asociada antes de guardar.
- [ ] El listado refleja los cambios exitosos sobre razas.
- [ ] La relación funcional entre especies y razas quedó contemplada.
- [ ] Los riesgos sobre especies no disponibles y eliminación con dependencias quedaron registrados.

## Criterios de aceptación

- [ ] El usuario puede consultar razas existentes.
- [ ] El usuario puede crear una raza.
- [ ] El usuario puede editar una raza existente.
- [ ] El usuario puede eliminar una raza existente.
- [ ] El nombre de la raza es obligatorio.
- [ ] La especie asociada es obligatoria.
- [ ] Si faltan datos obligatorios, el sistema impide el guardado y muestra validación.
- [ ] Si los datos son válidos, la raza queda asociada a la especie seleccionada.

## Escenarios funcionales

### Escenario: Crear raza asociada a especie

Dado que existe una especie registrada,  
cuando el usuario crea una raza con la información obligatoria,  
entonces el sistema guarda la raza  
y la asocia a la especie seleccionada.

### Escenario: Validar datos obligatorios de raza

Dado que el usuario se encuentra en el formulario de razas,  
cuando intenta guardar sin nombre o sin especie asociada,  
entonces el sistema impide el registro  
y muestra validación de campos obligatorios.

## Validaciones necesarias

- El nombre de la raza es obligatorio.
- La especie asociada es obligatoria.
- La especie debe existir.

## Reglas de negocio

- Toda raza debe pertenecer a una especie.
- No se confirman reglas taxonómicas adicionales.

## Dependencias funcionales

- Listado de razas.
- Formulario de creación y edición de razas.
- Especies previamente registradas.

## Tareas de implementación

- [ ] Verificar la capacidad actual de consultar razas.
- [ ] Documentar el formulario vigente de razas.
- [ ] Verificar la asociación actual con especies.
- [ ] Validar las reglas obligatorias observadas.
- [ ] Verificar los mensajes de éxito y error observados.
- [ ] Probar el flujo con datos válidos, inválidos y registros inexistentes.

## Riesgos o consideraciones

- El comportamiento exacto ante especies no disponibles debe validarse si el alcance funcional cambia.
- Requiere especies previamente registradas.
- La implementación observada no confirma todavía un bloqueo funcional de eliminación cuando existan mascotas asociadas.
