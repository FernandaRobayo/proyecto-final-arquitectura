# HU-11 - Protección de rutas y acceso a módulos

## Historia de Usuario

Como usuario interno autenticado,  
quiero acceder únicamente a los módulos protegidos cuando tengo una sesión activa,  
para evitar el uso no autorizado de la información operativa y clínica del sistema.

## Descripción funcional

El sistema debe controlar el acceso a las vistas protegidas y redirigir al usuario cuando intenta ingresar sin autenticación o cuando ya tiene una sesión activa y quiere volver a la pantalla de ingreso.

## Módulo

Control de acceso.

## Capa

Frontend con consumo de autenticación existente del backend.

## Prioridad

Alta.

## Estimación

3 puntos Scrum.

## Definición de Ready (DoR)

- [ ] La necesidad de proteger los módulos internos está claramente definida.
- [ ] Se encuentran identificadas las rutas de acceso público y protegido.
- [ ] Se conocen los comportamientos esperados con y sin sesión activa.
- [ ] Los vacíos sobre permisos por perfil están marcados como `Requiere validación`.

## Definición de Done (DoD)

- [ ] La historia documenta criterios visibles y escenarios funcionales de control de acceso.
- [ ] Se verifica la protección de módulos cuando no existe sesión activa.
- [ ] Se verifica la redirección al panel principal cuando el usuario ya está autenticado.
- [ ] Los límites actuales del control por perfil quedaron registrados.

## Criterios de aceptación

- [ ] El usuario sin sesión activa no puede acceder a módulos protegidos.
- [ ] El usuario sin sesión activa es redirigido al inicio de sesión.
- [ ] El usuario con sesión activa no permanece en la pantalla de inicio de sesión.
- [ ] El usuario autenticado puede acceder a los módulos protegidos disponibles.

## Escenarios funcionales

### Escenario: Acceso a módulo protegido con sesión activa

Dado que el usuario tiene una sesión activa,  
cuando intenta ingresar a un módulo protegido,  
entonces el sistema permite el acceso al módulo solicitado.

### Escenario: Acceso a módulo protegido sin sesión activa

Dado que el usuario no tiene una sesión activa,  
cuando intenta ingresar a un módulo protegido,  
entonces el sistema rechaza el acceso  
y redirige al inicio de sesión.

### Escenario: Intento de volver al inicio de sesión con sesión activa

Dado que el usuario ya se encuentra autenticado,  
cuando intenta ingresar nuevamente a la pantalla de inicio de sesión,  
entonces el sistema evita permanecer en esa vista  
y redirige al panel principal.

## Validaciones necesarias

- Debe existir una sesión activa para acceder a módulos protegidos.
- El acceso sin autenticación debe ser controlado.

## Reglas de negocio

- El acceso al sistema está orientado a usuarios internos autenticados.
- No se confirma una matriz detallada de permisos por perfil.

## Dependencias funcionales

- Inicio de sesión.
- Panel principal.
- Módulos protegidos del sistema.

## Tareas de implementación

- [ ] Verificar el comportamiento actual de acceso a módulos protegidos.
- [ ] Documentar las redirecciones observadas con y sin sesión activa.
- [ ] Validar el comportamiento de acceso al inicio de sesión cuando el usuario ya está autenticado.
- [ ] Verificar los límites actuales del control de acceso por perfil.

## Riesgos o consideraciones

- No se confirma una autorización completa por permisos detallados.
- La diferenciación funcional por rol requiere validación.
