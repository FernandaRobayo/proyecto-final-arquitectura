# HU-02 - Consulta de dashboard operativo

## Historia de Usuario

Como usuario interno de la veterinaria,  
quiero consultar un dashboard operativo,  
para visualizar información rápida de la operación diaria y acceder a acciones frecuentes.

## Descripción funcional

El panel principal debe presentar un resumen de la operación diaria utilizando datos de mascotas, citas y tratamientos. También debe facilitar la navegación hacia acciones frecuentes del sistema. La vista actual refleja información operativa general, pero no todos los indicadores están completamente resueltos.

## Módulo

Dashboard.

## Capa

Frontend con consumo de información existente del backend.

## Prioridad

Media.

## Estimación

3 puntos Scrum.

## Definición de Ready (DoR)

- [ ] La necesidad de consulta operativa del panel principal está claramente definida.
- [ ] Se encuentran identificados los datos operativos que alimentan el dashboard.
- [ ] Está identificado el usuario que consultará la información resumida.
- [ ] Se encuentran marcados como `Requiere validación` los indicadores no confirmados.
- [ ] Se conocen las dependencias funcionales con la información de mascotas, citas y tratamientos.

## Definición de Done (DoD)

- [ ] La historia documenta la consulta del dashboard con criterios visibles y escenarios funcionales.
- [ ] El panel principal presenta la información operativa esperada de forma comprensible.
- [ ] El flujo contempla comportamiento controlado cuando no hay datos disponibles.
- [ ] Los riesgos sobre indicadores no confirmados quedaron registrados.
- [ ] La historia puede evaluarse sin depender del tablero de Trello.

## Criterios de aceptación

- [ ] El usuario autenticado puede acceder al dashboard.
- [ ] El dashboard presenta información resumida de mascotas, citas y tratamientos.
- [ ] El dashboard muestra métricas o resúmenes operativos cuando existen datos disponibles.
- [ ] El dashboard muestra accesos rápidos hacia acciones frecuentes.
- [ ] Si no existen datos, el dashboard muestra estados informativos controlados.
- [ ] Si se presenta un error de carga, el dashboard conserva un estado visual estable.

## Escenarios funcionales

### Escenario: Visualización del dashboard

Dado que el usuario interno tiene una sesión activa,  
cuando ingresa al dashboard,  
entonces el sistema muestra el panel principal  
y presenta información operativa disponible.

### Escenario: Consulta de datos operativos

Dado que existen datos operativos registrados,  
cuando el dashboard carga la información,  
entonces el sistema muestra métricas o resúmenes asociados a la operación diaria.

### Escenario: Dashboard sin datos o con error controlado

Dado que no existen registros o se presenta una falla controlada de carga,  
cuando el dashboard intenta mostrar la información,  
entonces el sistema mantiene la vista disponible  
y presenta estados informativos sin bloquear al usuario.

## Validaciones necesarias

- El usuario debe tener una sesión activa.
- Debe existir disponibilidad de información para construir los resúmenes.
- El sistema debe manejar de forma controlada la ausencia de datos.

## Reglas de negocio

- El dashboard utiliza información de módulos ya existentes.
- El panel principal no reemplaza la consulta detallada de cada módulo.

## Dependencias funcionales

- Panel principal del sistema.
- Información de mascotas.
- Información de citas.
- Información de tratamientos.
- Accesos rápidos a funcionalidades frecuentes.

## Tareas de implementación

- [ ] Verificar la vista actual del dashboard.
- [ ] Documentar la carga vigente de información resumida.
- [ ] Verificar las tarjetas o métricas visibles.
- [ ] Verificar los accesos rápidos disponibles.
- [ ] Validar los estados vacíos y de error observados.
- [ ] Probar el dashboard con datos, sin datos y con errores controlados.

## Riesgos o consideraciones

- La métrica de pacientes activos no está resuelta de forma completa en la implementación observada y debe tratarse como brecha funcional.
- No se confirma una fuente exclusiva de datos distinta de los módulos existentes.
