# HU-08 - Gestión de citas

## Historia de Usuario

Como personal operativo de la veterinaria,  
quiero programar y administrar citas veterinarias,  
para coordinar la atención de mascotas con veterinarios internos.

## Descripción funcional

El sistema debe permitir programar, consultar, actualizar y eliminar citas veterinarias. Cada cita debe asociarse a una mascota y a un veterinario existente.

## Módulo

Citas.

## Capa

Fullstack.

## Prioridad

Alta.

## Estimación

5 puntos Scrum.

## Definición de Ready (DoR)

- [ ] La necesidad de programar y administrar citas veterinarias está claramente definida.
- [ ] Se encuentran identificados los datos obligatorios de la cita.
- [ ] Están identificadas las dependencias funcionales con mascotas y veterinarios.
- [ ] Se encuentran definidas las operaciones de consulta, registro, edición y eliminación.
- [ ] Las reglas no confirmadas sobre estados y agenda están registradas como `Requiere validación` o riesgo.

## Definición de Done (DoD)

- [ ] La historia documenta criterios visibles para consultar, crear, editar y eliminar citas.
- [ ] El sistema valida los datos obligatorios antes de guardar.
- [ ] La cita queda asociada a una mascota y a un veterinario válidos.
- [ ] El listado refleja los cambios exitosos sobre citas.
- [ ] Los riesgos sobre cancelación, reprogramación y estados quedaron registrados.

## Criterios de aceptación

- [ ] El usuario puede consultar citas existentes.
- [ ] El usuario puede crear una cita con datos válidos.
- [ ] El usuario puede editar una cita existente.
- [ ] El usuario puede eliminar una cita existente.
- [ ] La mascota es obligatoria.
- [ ] El veterinario es obligatorio.
- [ ] La fecha y hora de la cita son obligatorias.
- [ ] El motivo de la cita es obligatorio.
- [ ] El estado de la cita es obligatorio.
- [ ] Si faltan datos obligatorios, el sistema impide el guardado y muestra validación.
- [ ] Si la operación es exitosa, la cita queda visible en el listado actualizado.

## Escenarios funcionales

### Escenario: Crear cita exitosamente

Dado que existen mascotas y veterinarios registrados,  
cuando el usuario diligencia la información obligatoria de la cita y selecciona la mascota y el veterinario,  
entonces el sistema guarda la cita  
y la muestra en el listado.

### Escenario: Validar campos obligatorios de cita

Dado que el usuario se encuentra en el formulario de citas,  
cuando intenta guardar sin mascota, sin veterinario, sin fecha, sin motivo o sin estado,  
entonces el sistema impide el registro  
y muestra validación de campos obligatorios.

### Escenario: Editar cita existente

Dado que existe una cita registrada,  
cuando el usuario actualiza la fecha, el motivo o el estado y guarda los cambios,  
entonces el sistema actualiza la cita  
y refleja el cambio en el listado.

## Validaciones necesarias

- La mascota es obligatoria.
- El veterinario es obligatorio.
- La fecha y hora son obligatorias.
- El motivo de la cita es obligatorio.
- El estado de la cita es obligatorio.
- La mascota debe existir.
- El veterinario debe existir.

## Reglas de negocio

- Toda cita debe asociarse a una mascota.
- Toda cita debe asociarse a un veterinario.

## Dependencias funcionales

- Listado de citas.
- Formulario de creación y edición de citas.
- Mascotas previamente registradas.
- Veterinarios previamente registrados.

## Tareas de implementación

- [ ] Verificar la capacidad actual de consultar citas.
- [ ] Documentar el formulario vigente de citas.
- [ ] Verificar la selección actual de mascota y veterinario.
- [ ] Validar las reglas obligatorias observadas.
- [ ] Verificar los mensajes de éxito y error observados.
- [ ] Probar el flujo con datos válidos, inválidos y registros inexistentes.

## Riesgos o consideraciones

- El catálogo definitivo de estados requiere validación.
- No se confirma validación de disponibilidad horaria.
- No se confirma cruce de agenda.
- No se confirma bloqueo de eliminación cuando una cita tiene historia clínica asociada.
- La cancelación y la reprogramación no se documentan como flujos independientes.
