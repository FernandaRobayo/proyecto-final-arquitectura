# HU-09 - Gestión de historias clínicas

## Historia de Usuario

Como personal operativo o veterinario interno,  
quiero registrar y consultar información clínica,  
para documentar diagnósticos y observaciones asociados a una cita.

## Descripción funcional

El sistema debe permitir consultar, registrar, actualizar y eliminar historias clínicas. Cada historia clínica debe asociarse a una cita existente. Además del diagnóstico, pueden registrarse datos complementarios como notas, peso y temperatura cuando estén disponibles.

## Módulo

Historias clínicas.

## Capa

Fullstack.

## Prioridad

Alta.

## Estimación

5 puntos Scrum.

## Definición de Ready (DoR)

- [ ] La necesidad de registrar información clínica asociada a una cita está claramente definida.
- [ ] Se encuentran identificados los datos obligatorios de la historia clínica.
- [ ] Está identificada la dependencia funcional con citas previamente registradas.
- [ ] Se encuentran definidas las operaciones que harán parte del alcance actual.
- [ ] Las reglas no confirmadas sobre unicidad por cita y manejo de información sensible quedaron registradas como riesgo o `Requiere validación`.

## Definición de Done (DoD)

- [ ] La historia documenta criterios visibles para consultar, crear, editar y eliminar historias clínicas según el alcance definido.
- [ ] El sistema valida la cita asociada y el diagnóstico antes de guardar.
- [ ] La historia clínica queda asociada a una cita existente.
- [ ] El listado o consulta refleja los cambios exitosos sobre historias clínicas.
- [ ] Los riesgos sobre información clínica sensible y dependencias quedaron registrados.

## Criterios de aceptación

- [ ] El usuario puede consultar historias clínicas existentes.
- [ ] El usuario puede crear una historia clínica con datos válidos.
- [ ] El usuario puede editar una historia clínica existente.
- [ ] El usuario puede eliminar una historia clínica existente.
- [ ] La cita asociada es obligatoria.
- [ ] El diagnóstico es obligatorio.
- [ ] Las notas, el peso y la temperatura pueden registrarse como datos complementarios.
- [ ] Si faltan datos obligatorios, el sistema impide el guardado y muestra validación.
- [ ] Si la operación es exitosa, la historia clínica queda visible en el listado actualizado.

## Escenarios funcionales

### Escenario: Registrar historia clínica exitosamente

Dado que existe una cita registrada,  
cuando el usuario diligencia el diagnóstico y selecciona la cita asociada,  
entonces el sistema guarda la historia clínica  
y la asocia a la cita seleccionada.

### Escenario: Validar campos obligatorios de historia clínica

Dado que el usuario se encuentra en el formulario de historias clínicas,  
cuando intenta guardar sin cita asociada o sin diagnóstico,  
entonces el sistema impide el registro  
y muestra validación de campos obligatorios.

### Escenario: Rechazar una historia clínica para una cita no válida

Dado que el usuario se encuentra en el formulario de historias clínicas,  
cuando intenta registrar una historia clínica asociada a una cita no válida,  
entonces el sistema rechaza la operación  
y muestra un mensaje indicando que la cita asociada no es válida.

## Validaciones necesarias

- La cita asociada es obligatoria.
- El diagnóstico es obligatorio.
- La cita debe existir.

## Reglas de negocio

- Toda historia clínica debe asociarse a una cita existente.
- Se debe validar si cada cita puede tener máximo una historia clínica asociada.
- No se confirman flujos clínicos adicionales.

## Dependencias funcionales

- Listado de historias clínicas.
- Formulario de creación y edición de historias clínicas.
- Citas previamente registradas.

## Tareas de implementación

- [ ] Verificar la capacidad actual de consultar historias clínicas.
- [ ] Documentar el formulario vigente de historias clínicas.
- [ ] Verificar la selección actual de cita asociada.
- [ ] Validar las reglas obligatorias observadas.
- [ ] Verificar los mensajes de éxito y error observados.
- [ ] Probar el flujo con datos válidos, inválidos y registros inexistentes.

## Riesgos o consideraciones

- La restricción de una historia clínica por cita debe validarse como regla funcional.
- La edición o eliminación de historias clínicas debe validarse por tratarse de información clínica sensible.
- No se confirman reportes médicos ni flujos clínicos adicionales.
- La implementación observada no confirma todavía un bloqueo funcional de eliminación cuando existan tratamientos asociados.
