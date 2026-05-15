# HU-05 - Gestión de especies

## Historia de Usuario

Como personal operativo de la veterinaria,  
quiero registrar y actualizar especies,  
para mantener actualizado el catálogo base de animales del sistema.

## Descripción funcional

El sistema debe permitir consultar, registrar, actualizar y eliminar especies. Las especies sirven como base para clasificar razas y mascotas.

## Módulo

Especies.

## Capa

Fullstack.

## Prioridad

Media.

## Estimación

3 puntos Scrum.

## Definición de Ready (DoR)

- [ ] La necesidad de administrar el catálogo de especies está claramente definida.
- [ ] Se encuentra identificado el dato obligatorio para registrar una especie.
- [ ] Está identificada la relación funcional entre especies y razas.
- [ ] Se encuentran definidas las operaciones de consulta, registro, edición y eliminación.
- [ ] Los riesgos sobre eliminación con dependencias fueron identificados.

## Definición de Done (DoD)

- [ ] La historia documenta criterios visibles para consultar, crear, editar y eliminar especies.
- [ ] El sistema valida el nombre obligatorio antes de guardar.
- [ ] El listado refleja los cambios exitosos sobre el catálogo de especies.
- [ ] La dependencia funcional con razas quedó contemplada.
- [ ] Las brechas sobre eliminación con dependencias quedaron registradas.

## Criterios de aceptación

- [ ] El usuario puede consultar especies existentes.
- [ ] El usuario puede crear una especie.
- [ ] El usuario puede editar una especie existente.
- [ ] El usuario puede eliminar una especie existente.
- [ ] El nombre de la especie es obligatorio.
- [ ] Si falta el nombre, el sistema impide el guardado y muestra validación.
- [ ] Si la operación es exitosa, la especie queda disponible en el listado.

## Escenarios funcionales

### Escenario: Crear especie exitosamente

Dado que el usuario se encuentra en el módulo de especies,  
cuando registra el nombre de una especie y confirma el guardado,  
entonces el sistema crea la especie  
y la muestra en el listado.

### Escenario: Validar nombre obligatorio de especie

Dado que el usuario se encuentra en el formulario de especies,  
cuando intenta guardar sin diligenciar el nombre,  
entonces el sistema impide el registro  
y muestra validación del campo obligatorio.

### Escenario: Editar especie existente

Dado que existe una especie registrada,  
cuando el usuario actualiza su nombre y guarda,  
entonces el sistema actualiza la especie  
y refleja el cambio en el listado.

## Validaciones necesarias

- El nombre de la especie es obligatorio.
- El registro debe existir para permitir edición o eliminación.

## Reglas de negocio

- Las especies se usan como catálogo base.
- No se confirman taxonomías adicionales.

## Dependencias funcionales

- Listado de especies.
- Formulario de creación y edición de especies.
- Catálogo base para razas y mascotas.

## Tareas de implementación

- [ ] Verificar la capacidad actual de consultar especies.
- [ ] Documentar el formulario vigente de especies.
- [ ] Validar la regla actual del nombre obligatorio.
- [ ] Verificar el comportamiento actual de eliminación de registros.
- [ ] Verificar los mensajes de éxito y error observados.
- [ ] Probar el flujo con datos válidos, inválidos y registros inexistentes.

## Riesgos o consideraciones

- La implementación observada no confirma todavía un bloqueo funcional de eliminación cuando existan razas asociadas.
- Si se decide proteger la trazabilidad, debe validarse la eliminación de especies con dependencias activas.
