# HU-10 - Gestión de tratamientos

## Historia de Usuario

Como personal operativo o veterinario interno,  
quiero registrar tratamientos asociados a diagnósticos,  
para documentar las indicaciones terapéuticas definidas durante la atención veterinaria.

## Descripción funcional

El sistema debe permitir consultar, registrar, actualizar y eliminar tratamientos. Cada tratamiento debe asociarse a una historia clínica existente. Además de la descripción general, pueden registrarse datos complementarios como medicación, dosis y duración cuando estén disponibles.

## Módulo

Tratamientos.

## Capa

Fullstack.

## Prioridad

Alta.

## Estimación

5 puntos Scrum.

## Definición de Ready (DoR)

- [ ] La necesidad de registrar tratamientos asociados a una historia clínica está claramente definida.
- [ ] Se encuentran identificados los datos obligatorios del tratamiento.
- [ ] Está identificada la dependencia funcional con historias clínicas previamente registradas.
- [ ] Se encuentran definidas las operaciones incluidas en el alcance actual.
- [ ] Los comportamientos no confirmados sobre detalle terapéutico adicional quedaron registrados como riesgo o `Requiere validación`.

## Definición de Done (DoD)

- [ ] La historia documenta criterios visibles para consultar, crear, editar y eliminar tratamientos según el alcance definido.
- [ ] El sistema valida la historia clínica asociada y la descripción del tratamiento antes de guardar.
- [ ] El tratamiento queda asociado a una historia clínica existente.
- [ ] El listado o consulta refleja los cambios exitosos sobre tratamientos.
- [ ] Los riesgos sobre detalle clínico adicional y dependencias quedaron registrados.

## Criterios de aceptación

- [ ] El usuario puede consultar tratamientos existentes.
- [ ] El usuario puede crear un tratamiento con datos válidos.
- [ ] El usuario puede editar un tratamiento existente.
- [ ] El usuario puede eliminar un tratamiento existente.
- [ ] La historia clínica asociada es obligatoria.
- [ ] La descripción del tratamiento es obligatoria.
- [ ] La medicación, la dosis y la duración pueden registrarse como datos complementarios.
- [ ] Si faltan datos obligatorios, el sistema impide el guardado y muestra validación.
- [ ] Si la operación es exitosa, el tratamiento queda visible en el listado actualizado.

## Escenarios funcionales

### Escenario: Registrar tratamiento exitosamente

Dado que existe una historia clínica registrada,  
cuando el usuario diligencia la descripción del tratamiento y selecciona la historia clínica asociada,  
entonces el sistema guarda el tratamiento  
y lo muestra en el listado.

### Escenario: Validar campos obligatorios de tratamiento

Dado que el usuario se encuentra en el formulario de tratamientos,  
cuando intenta guardar sin historia clínica asociada o sin descripción,  
entonces el sistema impide el registro  
y muestra validación de campos obligatorios.

### Escenario: Rechazar un tratamiento con historia clínica no válida

Dado que el usuario se encuentra en el formulario de tratamientos,  
cuando intenta registrar un tratamiento asociado a una historia clínica no válida,  
entonces el sistema rechaza la operación  
y muestra un mensaje indicando que la historia clínica asociada no es válida.

## Validaciones necesarias

- La historia clínica asociada es obligatoria.
- La descripción del tratamiento es obligatoria.
- La historia clínica debe existir.

## Reglas de negocio

- Todo tratamiento debe asociarse a una historia clínica.
- No se confirman integraciones externas.

## Dependencias funcionales

- Listado de tratamientos.
- Formulario de creación y edición de tratamientos.
- Historias clínicas previamente registradas.

## Tareas de implementación

- [ ] Verificar la capacidad actual de consultar tratamientos.
- [ ] Documentar el formulario vigente de tratamientos.
- [ ] Verificar la selección actual de historia clínica asociada.
- [ ] Validar las reglas obligatorias observadas.
- [ ] Verificar los mensajes de éxito y error observados.
- [ ] Probar el flujo con datos válidos, inválidos y registros inexistentes.

## Riesgos o consideraciones

- Se debe validar si una historia clínica puede tener uno o varios tratamientos asociados.
- Se debe validar si el tratamiento requiere campos adicionales distintos de medicación, dosis y duración.
- No se confirman reglas farmacológicas.
- No se confirman integraciones externas.
