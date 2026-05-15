# HU-13 - Página no encontrada

## Historia de Usuario

Como usuario interno de la veterinaria,  
quiero recibir una vista controlada cuando ingreso a una ruta no disponible,  
para entender que la opción no existe y poder regresar a una ruta válida.

## Descripción funcional

El sistema debe presentar una vista de página no encontrada cuando el usuario intenta acceder a una dirección no disponible dentro de la aplicación, evitando que quede en un estado confuso.

## Módulo

Navegación y rutas.

## Capa

Frontend.

## Prioridad

Media.

## Estimación

2 puntos Scrum.

## Definición de Ready (DoR)

- [ ] La necesidad de controlar rutas no disponibles está claramente definida.
- [ ] Se encuentra identificado el comportamiento esperado ante una navegación inválida.
- [ ] Se conoce la existencia de una vista de página no encontrada.

## Definición de Done (DoD)

- [ ] La historia documenta criterios visibles y escenarios funcionales de navegación inválida.
- [ ] Se verifica la visualización de una vista controlada cuando la ruta no existe.
- [ ] Se verifica que el usuario pueda regresar a una opción válida.

## Criterios de aceptación

- [ ] El usuario visualiza una página no encontrada cuando intenta acceder a una ruta no disponible.
- [ ] La navegación inválida no deja al usuario en un estado bloqueado.
- [ ] El usuario puede regresar desde la vista de página no encontrada.

## Escenarios funcionales

### Escenario: Navegación a una ruta no disponible

Dado que el usuario intenta acceder a una ruta que no existe,  
cuando el sistema procesa la navegación,  
entonces presenta una vista de página no encontrada.

### Escenario: Regreso desde la vista no encontrada

Dado que el usuario se encuentra en la vista de página no encontrada,  
cuando decide regresar,  
entonces el sistema lo lleva a una ruta válida o a la navegación anterior disponible.

## Validaciones necesarias

- La vista de página no encontrada debe mostrarse ante rutas inválidas.
- El usuario debe contar con una alternativa de regreso.

## Reglas de negocio

- La navegación inválida debe resolverse de forma controlada.
- No se documentan rutas externas dentro del alcance actual.

## Dependencias funcionales

- Navegación general del sistema.
- Vista de página no encontrada.

## Tareas de implementación

- [ ] Verificar el comportamiento actual ante rutas no disponibles.
- [ ] Documentar la vista observada de página no encontrada.
- [ ] Validar la opción de regreso disponible para el usuario.

## Riesgos o consideraciones

- No se confirma una personalización adicional de la vista de página no encontrada.
