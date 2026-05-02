# Nivel 3 - Componentes

## Descripcion

Este diagrama amplia el contenedor `Backend API` y muestra los componentes principales confirmados en el backend. Se limita a la estructura real observada en el codigo: controladores, servicios y repositorios.

## Diagrama

- [c4-level-3-components.puml](C:/www/proyecto-final-arquitectura/docs/02-architecture/diagrams/as-is/c4-level-3-components.puml)

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
  Delegan autenticacion y operaciones CRUD.
- `Servicios -> Repositorios JPA`
  Ejecutan validaciones, consultas y persistencia.
- `Repositorios JPA -> Base de datos MySQL`
  Persisten y recuperan entidades del sistema.

## Observaciones

- Este nivel no incluye frontend, porque la solicitud exige que el nivel 3 se concentre unicamente en el backend.
- Para mantener claridad visual, el diagrama agrupa componentes por capa, aunque los nombres reales incluidos corresponden a clases e interfaces confirmadas del proyecto.
