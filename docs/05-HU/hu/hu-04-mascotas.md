# HU-04 - Gestión de mascotas

## Historia de Usuario

Como personal operativo de la veterinaria,  
quiero registrar y actualizar pacientes veterinarios,  
para mantener actualizada la información de las mascotas asociadas a clientes y razas.

## Descripción funcional

El sistema debe permitir consultar, registrar, actualizar y eliminar mascotas. Cada mascota debe estar asociada a un cliente y a una raza existente. Además del nombre, pueden registrarse datos complementarios como género, fecha de nacimiento y color cuando estén disponibles.

## Módulo

Mascotas.

## Capa

Fullstack.

## Prioridad

Alta.

## Estimación

5 puntos Scrum.

## Definición de Ready (DoR)

- [ ] La necesidad de registrar y actualizar pacientes veterinarios está claramente entendida.
- [ ] Se encuentran identificados los datos obligatorios de la mascota.
- [ ] Están identificadas las dependencias funcionales con clientes y razas.
- [ ] Se encuentran definidas las operaciones de consulta, registro, edición y eliminación.
- [ ] Los elementos no confirmados, como el catálogo definitivo de género, están marcados como `Requiere validación`.

## Definición de Done (DoD)

- [ ] La historia documenta criterios visibles para consultar, crear, editar y eliminar mascotas.
- [ ] El sistema valida el nombre y las asociaciones obligatorias antes de guardar.
- [ ] La mascota queda asociada a un cliente y a una raza válidos.
- [ ] El listado refleja los cambios exitosos sobre mascotas.
- [ ] Las brechas sobre eliminación con dependencias quedaron contempladas.

## Criterios de aceptación

- [ ] El usuario puede consultar el listado de mascotas.
- [ ] El usuario puede registrar una nueva mascota con datos válidos.
- [ ] El usuario puede editar una mascota existente.
- [ ] El usuario puede eliminar una mascota existente.
- [ ] El nombre de la mascota es obligatorio.
- [ ] La mascota debe estar asociada a un cliente.
- [ ] La mascota debe estar asociada a una raza.
- [ ] El género, la fecha de nacimiento y el color pueden registrarse como datos complementarios.
- [ ] Si faltan datos obligatorios, el sistema impide el guardado y muestra validación.
- [ ] Si la operación es exitosa, la mascota queda visible en el listado actualizado.

## Escenarios funcionales

### Escenario: Registrar mascota exitosamente

Dado que existen clientes y razas registrados,  
cuando el usuario diligencia la información obligatoria de la mascota y selecciona el cliente y la raza asociados,  
entonces el sistema guarda la mascota  
y la muestra en el listado.

### Escenario: Validar campos obligatorios de mascota

Dado que el usuario se encuentra en el formulario de mascotas,  
cuando intenta guardar sin nombre, sin cliente o sin raza,  
entonces el sistema impide el registro  
y muestra validación en los campos requeridos.

### Escenario: Editar mascota existente

Dado que existe una mascota registrada,  
cuando el usuario modifica sus datos y guarda los cambios,  
entonces el sistema actualiza la información de la mascota  
y refleja el cambio en el listado.

## Validaciones necesarias

- El nombre de la mascota es obligatorio.
- El cliente asociado es obligatorio.
- La raza asociada es obligatoria.
- El cliente debe existir.
- La raza debe existir.

## Reglas de negocio

- Toda mascota debe estar asociada a un cliente.
- Toda mascota debe estar asociada a una raza.

## Dependencias funcionales

- Listado de mascotas.
- Formulario de creación y edición de mascotas.
- Clientes previamente registrados.
- Razas previamente registradas.

## Tareas de implementación

- [ ] Verificar la capacidad actual de consultar mascotas.
- [ ] Documentar el formulario vigente de creación y edición.
- [ ] Verificar la selección actual de cliente y raza.
- [ ] Validar las reglas obligatorias observadas.
- [ ] Verificar los mensajes de éxito y error observados.
- [ ] Probar el flujo con datos válidos, inválidos y registros inexistentes.

## Riesgos o consideraciones

- El catálogo definitivo de género requiere validación.
- Requiere clientes y razas previamente registrados.
- La implementación observada no confirma todavía un bloqueo funcional de eliminación cuando existan citas asociadas.
