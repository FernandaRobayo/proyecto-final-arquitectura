# HU-03 - Gestión de clientes

## Historia de Usuario

Como personal operativo de la veterinaria,  
quiero registrar y actualizar clientes propietarios,  
para mantener actualizada la información de las personas responsables de las mascotas.

## Descripción funcional

El sistema debe permitir consultar, registrar, actualizar y eliminar clientes propietarios. Esta información se utiliza para asociar mascotas a sus responsables dentro del proceso operativo. Además del nombre completo y el documento, pueden registrarse datos complementarios como teléfono, correo y dirección cuando estén disponibles.

## Módulo

Clientes.

## Capa

Fullstack.

## Prioridad

Alta.

## Estimación

5 puntos Scrum.

## Definición de Ready (DoR)

- [ ] La necesidad de registrar y actualizar clientes propietarios está claramente definida.
- [ ] Se encuentran identificados los datos obligatorios del cliente.
- [ ] Está claro que la información del cliente servirá para asociar mascotas.
- [ ] Se encuentran identificadas las operaciones de consulta, registro, edición y eliminación.
- [ ] Los riesgos sobre eliminación con dependencias quedaron identificados.

## Definición de Done (DoD)

- [ ] La historia documenta criterios visibles para consultar, crear, editar y eliminar clientes.
- [ ] El sistema valida los datos obligatorios antes de guardar.
- [ ] El listado refleja los cambios exitosos sobre clientes.
- [ ] La relación funcional entre clientes y mascotas quedó contemplada.
- [ ] Las brechas sobre eliminación con dependencias quedaron registradas.

## Criterios de aceptación

- [ ] El usuario puede consultar el listado de clientes.
- [ ] El usuario puede registrar un nuevo cliente con datos válidos.
- [ ] El usuario puede editar un cliente existente.
- [ ] El usuario puede eliminar un cliente existente.
- [ ] El nombre completo del cliente es obligatorio.
- [ ] El número de documento del cliente es obligatorio.
- [ ] El teléfono, el correo y la dirección pueden registrarse como datos complementarios.
- [ ] Si faltan datos obligatorios, el sistema impide el guardado y muestra validación.
- [ ] Si la operación es exitosa, el listado se actualiza o el usuario recibe retroalimentación visual.

## Escenarios funcionales

### Escenario: Registrar cliente exitosamente

Dado que el usuario se encuentra en el módulo de clientes,  
cuando diligencia la información obligatoria del cliente y confirma el registro,  
entonces el sistema guarda el cliente  
y muestra la información actualizada en el listado.

### Escenario: Validar campos obligatorios de cliente

Dado que el usuario se encuentra en el formulario de clientes,  
cuando intenta guardar sin nombre completo o sin número de documento,  
entonces el sistema impide el registro  
y muestra validación sobre los campos obligatorios.

### Escenario: Editar cliente existente

Dado que existe un cliente registrado,  
cuando el usuario modifica sus datos y guarda los cambios,  
entonces el sistema actualiza el cliente  
y refleja la información actualizada en el listado.

## Validaciones necesarias

- El nombre completo del cliente es obligatorio.
- El número de documento del cliente es obligatorio.
- El registro debe existir para permitir edición o eliminación.

## Reglas de negocio

- El cliente representa al propietario de una o varias mascotas.
- No existe un portal confirmado para clientes externos.

## Dependencias funcionales

- Listado de clientes.
- Formulario de creación y edición de clientes.
- Asociación posterior entre clientes y mascotas.

## Tareas de implementación

- [ ] Verificar la capacidad actual de consultar clientes.
- [ ] Documentar el formulario vigente de creación y edición.
- [ ] Validar las reglas obligatorias observadas.
- [ ] Verificar el comportamiento actual de eliminación de registros.
- [ ] Verificar los mensajes de éxito y error observados.
- [ ] Probar el flujo con datos válidos, inválidos y registros inexistentes.

## Riesgos o consideraciones

- La implementación observada permite eliminación directa y no refleja todavía un bloqueo funcional por dependencias.
- Si se decide proteger la trazabilidad, debe validarse la política de eliminación cuando existan mascotas asociadas.
- No existe portal de clientes externos confirmado.
