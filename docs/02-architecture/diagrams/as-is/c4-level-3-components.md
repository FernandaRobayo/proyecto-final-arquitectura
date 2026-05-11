# Nivel 3 - Componentes

## Descripción

Este diagrama amplía el contenedor `Backend API` y muestra los componentes principales confirmados en el backend. Se limita a la estructura real observada en el código: controladores, servicios y repositorios.

## Diagrama

- [c4-level-3-components.png](C:/www/proyecto-final-arquitectura/docs/02-architecture/diagrams/as-is/c4-level-3-components.png)

## Elementos

- `Controladores REST`  
  `AuthRestController`, `CustomerRestController`, `SpeciesRestController`, `BreedRestController`, `PetRestController`, `VeterinarianRestController`, `AppointmentRestController`, `MedicalRecordRestController` y `TreatmentRestController`.

- `Servicios`  
  `UsuarioService`, `CustomerServiceImpl`, `SpeciesServiceImpl`, `BreedServiceImpl`, `PetServiceImpl`, `VeterinarianServiceImpl`, `AppointmentServiceImpl`, `MedicalRecordServiceImpl` y `TreatmentServiceImpl`.

- `Repositorios JPA`  
  `IUsuarioDao`, `ICustomerDao`, `ISpeciesDao`, `IBreedDao`, `IPetDao`, `IVeterinarianDao`, `IAppointmentDao`, `IMedicalRecordDao`, `ITreatmentDao` e `IRoleDao`.

- `Base de datos MySQL`  
  Contenedor externo consumido por los repositorios.

## Relaciones

- `Controladores REST -> Servicios`  
  Delegan autenticación y operaciones CRUD.

- `Servicios -> Repositorios JPA`  
  Ejecutan validaciones, consultas y persistencia.

- `Repositorios JPA -> Base de datos MySQL`  
  Acceden a la información persistida mediante JPA.