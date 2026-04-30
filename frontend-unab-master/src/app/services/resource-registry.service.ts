import { Injectable } from '@angular/core';
import { Observable, forkJoin, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Appointment } from '../models/appointment.model';
import { Breed } from '../models/breed.model';
import { Customer } from '../models/customer.model';
import { MedicalRecord } from '../models/medical-record.model';
import { Pet } from '../models/pet.model';
import { Treatment } from '../models/treatment.model';
import { Veterinarian } from '../models/veterinarian.model';
import { AppointmentService } from './appointment.service';
import { BreedService } from './breed.service';
import { CrudService } from './base-resource.service';
import { CustomerService } from './customer.service';
import { MedicalRecordService } from './medical-record.service';
import { PetService } from './pet.service';
import { SpeciesService } from './species.service';
import { TreatmentService } from './treatment.service';
import { VeterinarianService } from './veterinarian.service';

export type ResourceKey =
  | 'customers'
  | 'species'
  | 'breeds'
  | 'pets'
  | 'veterinarians'
  | 'appointments'
  | 'medical-records'
  | 'treatments';

export interface SelectOption {
  value: number | string;
  label: string;
}

export interface ResourceColumnConfig {
  key: string;
  label: string;
  type?: 'text' | 'date' | 'datetime' | 'number';
}

export interface ResourceFieldConfig {
  key: string;
  label: string;
  type: 'text' | 'textarea' | 'email' | 'date' | 'datetime-local' | 'number' | 'select';
  required?: boolean;
  placeholder?: string;
  optionResource?: ResourceKey;
  options?: SelectOption[];
  valueType?: 'string' | 'number';
}

export interface ResourceNavigationItem {
  key: ResourceKey;
  label: string;
  path: string;
  description: string;
}

export interface ResourceConfig {
  key: ResourceKey;
  routeSegment: ResourceKey;
  path: string;
  label: string;
  singularLabel: string;
  description: string;
  columns: ResourceColumnConfig[];
  fields: ResourceFieldConfig[];
  service: CrudService<any>;
}

@Injectable({
  providedIn: 'root'
})
export class ResourceRegistryService {
  private readonly configs: Record<ResourceKey, ResourceConfig>;

  constructor(
    private customerService: CustomerService,
    private speciesService: SpeciesService,
    private breedService: BreedService,
    private petService: PetService,
    private veterinarianService: VeterinarianService,
    private appointmentService: AppointmentService,
    private medicalRecordService: MedicalRecordService,
    private treatmentService: TreatmentService
  ) {
    this.configs = {
      customers: {
        key: 'customers',
        routeSegment: 'customers',
        path: '/customers',
        label: 'Customers',
        singularLabel: 'customer',
        description: 'Gestiona clientes y propietarios.',
        service: this.customerService,
        columns: [
          { key: 'fullName', label: 'Nombre completo' },
          { key: 'documentNumber', label: 'Documento' },
          { key: 'phone', label: 'Telefono' },
          { key: 'email', label: 'Correo' }
        ],
        fields: [
          { key: 'fullName', label: 'Nombre completo', type: 'text', required: true },
          { key: 'documentNumber', label: 'Documento', type: 'text', required: true },
          { key: 'phone', label: 'Telefono', type: 'text' },
          { key: 'email', label: 'Correo', type: 'email' },
          { key: 'address', label: 'Direccion', type: 'textarea' }
        ]
      },
      species: {
        key: 'species',
        routeSegment: 'species',
        path: '/species',
        label: 'Species',
        singularLabel: 'species',
        description: 'Gestiona especies base del sistema.',
        service: this.speciesService,
        columns: [
          { key: 'name', label: 'Nombre' }
        ],
        fields: [
          { key: 'name', label: 'Nombre', type: 'text', required: true }
        ]
      },
      breeds: {
        key: 'breeds',
        routeSegment: 'breeds',
        path: '/breeds',
        label: 'Breeds',
        singularLabel: 'breed',
        description: 'Relaciona razas con sus especies.',
        service: this.breedService,
        columns: [
          { key: 'name', label: 'Nombre' },
          { key: 'speciesName', label: 'Especie' }
        ],
        fields: [
          { key: 'name', label: 'Nombre', type: 'text', required: true },
          { key: 'speciesId', label: 'Especie', type: 'select', required: true, optionResource: 'species', valueType: 'number' }
        ]
      },
      pets: {
        key: 'pets',
        routeSegment: 'pets',
        path: '/pets',
        label: 'Pets',
        singularLabel: 'pet',
        description: 'Administra mascotas y sus datos principales.',
        service: this.petService,
        columns: [
          { key: 'name', label: 'Nombre' },
          { key: 'customerFullName', label: 'Cliente' },
          { key: 'breedName', label: 'Raza' },
          { key: 'gender', label: 'Genero' }
        ],
        fields: [
          { key: 'customerId', label: 'Cliente', type: 'select', required: true, optionResource: 'customers', valueType: 'number' },
          { key: 'breedId', label: 'Raza', type: 'select', required: true, optionResource: 'breeds', valueType: 'number' },
          { key: 'name', label: 'Nombre', type: 'text', required: true },
          {
            key: 'gender',
            label: 'Genero',
            type: 'select',
            options: [
              { value: 'Male', label: 'Male' },
              { value: 'Female', label: 'Female' },
              { value: 'Unknown', label: 'Unknown' }
            ],
            valueType: 'string'
          },
          { key: 'birthDate', label: 'Fecha de nacimiento', type: 'date' },
          { key: 'color', label: 'Color', type: 'text' }
        ]
      },
      veterinarians: {
        key: 'veterinarians',
        routeSegment: 'veterinarians',
        path: '/veterinarians',
        label: 'Veterinarians',
        singularLabel: 'veterinarian',
        description: 'Administra profesionales y especialidades.',
        service: this.veterinarianService,
        columns: [
          { key: 'fullName', label: 'Nombre completo' },
          { key: 'documentNumber', label: 'Documento' },
          { key: 'specialty', label: 'Especialidad' },
          { key: 'email', label: 'Correo' }
        ],
        fields: [
          { key: 'fullName', label: 'Nombre completo', type: 'text', required: true },
          { key: 'documentNumber', label: 'Documento', type: 'text', required: true },
          { key: 'phone', label: 'Telefono', type: 'text' },
          { key: 'email', label: 'Correo', type: 'email' },
          { key: 'specialty', label: 'Especialidad', type: 'text' }
        ]
      },
      appointments: {
        key: 'appointments',
        routeSegment: 'appointments',
        path: '/appointments',
        label: 'Appointments',
        singularLabel: 'appointment',
        description: 'Programa y actualiza citas veterinarias.',
        service: this.appointmentService,
        columns: [
          { key: 'petName', label: 'Mascota' },
          { key: 'veterinarianFullName', label: 'Veterinario' },
          { key: 'appointmentDateTime', label: 'Fecha y hora', type: 'datetime' },
          { key: 'status', label: 'Estado' }
        ],
        fields: [
          { key: 'petId', label: 'Mascota', type: 'select', required: true, optionResource: 'pets', valueType: 'number' },
          { key: 'veterinarianId', label: 'Veterinario', type: 'select', required: true, optionResource: 'veterinarians', valueType: 'number' },
          { key: 'appointmentDateTime', label: 'Fecha y hora', type: 'datetime-local', required: true },
          { key: 'reason', label: 'Motivo', type: 'textarea', required: true },
          {
            key: 'status',
            label: 'Estado',
            type: 'select',
            required: true,
            options: [
              { value: 'Scheduled', label: 'Scheduled' },
              { value: 'Completed', label: 'Completed' },
              { value: 'Cancelled', label: 'Cancelled' }
            ],
            valueType: 'string'
          }
        ]
      },
      'medical-records': {
        key: 'medical-records',
        routeSegment: 'medical-records',
        path: '/medical-records',
        label: 'Medical Records',
        singularLabel: 'medical record',
        description: 'Registra diagnosticos y observaciones clinicas.',
        service: this.medicalRecordService,
        columns: [
          { key: 'appointmentId', label: 'Cita' },
          { key: 'diagnosis', label: 'Diagnostico' },
          { key: 'weight', label: 'Peso', type: 'number' },
          { key: 'temperature', label: 'Temperatura', type: 'number' }
        ],
        fields: [
          { key: 'appointmentId', label: 'Cita', type: 'select', required: true, optionResource: 'appointments', valueType: 'number' },
          { key: 'diagnosis', label: 'Diagnostico', type: 'textarea', required: true },
          { key: 'notes', label: 'Notas', type: 'textarea' },
          { key: 'weight', label: 'Peso', type: 'number' },
          { key: 'temperature', label: 'Temperatura', type: 'number' }
        ]
      },
      treatments: {
        key: 'treatments',
        routeSegment: 'treatments',
        path: '/treatments',
        label: 'Treatments',
        singularLabel: 'treatment',
        description: 'Asocia planes de tratamiento a una historia clinica.',
        service: this.treatmentService,
        columns: [
          { key: 'description', label: 'Descripcion' },
          { key: 'medication', label: 'Medicacion' },
          { key: 'dosage', label: 'Dosis' },
          { key: 'duration', label: 'Duracion' }
        ],
        fields: [
          { key: 'medicalRecordId', label: 'Historia clinica', type: 'select', required: true, optionResource: 'medical-records', valueType: 'number' },
          { key: 'description', label: 'Descripcion', type: 'textarea', required: true },
          { key: 'medication', label: 'Medicacion', type: 'text' },
          { key: 'dosage', label: 'Dosis', type: 'text' },
          { key: 'duration', label: 'Duracion', type: 'text' }
        ]
      }
    };
  }

  getConfig(resource: string): ResourceConfig {
    return this.configs[resource as ResourceKey];
  }

  getNavigationItems(): ResourceNavigationItem[] {
    return Object.values(this.configs).map((config) => ({
      key: config.key,
      label: config.label,
      path: config.path,
      description: config.description
    }));
  }

  loadOptions(config: ResourceConfig): Observable<Record<string, SelectOption[]>> {
    const selectFields = config.fields.filter((field) => field.type === 'select');

    if (!selectFields.length) {
      return of({});
    }

    const requests = selectFields.reduce((accumulator, field) => {
      accumulator[field.key] = this.getFieldOptions(field);
      return accumulator;
    }, {} as Record<string, Observable<SelectOption[]>>);

    return forkJoin(requests);
  }

  mapEntityToFormValues(config: ResourceConfig, entity: any): any {
    const values: any = {};

    config.fields.forEach((field) => {
      const rawValue = entity[field.key];

      if (field.type === 'date') {
        values[field.key] = rawValue ? String(rawValue).slice(0, 10) : '';
        return;
      }

      if (field.type === 'datetime-local') {
        values[field.key] = rawValue ? String(rawValue).slice(0, 16) : '';
        return;
      }

      values[field.key] = rawValue != null ? rawValue : '';
    });

    return values;
  }

  preparePayload(config: ResourceConfig, rawValue: any): any {
    const payload: any = {};

    config.fields.forEach((field) => {
      let value = rawValue[field.key];

      if (field.type === 'number') {
        payload[field.key] = value === '' || value == null ? null : Number(value);
        return;
      }

      if (field.type === 'select') {
        payload[field.key] = value === '' || value == null ? null : value;
        return;
      }

      if (field.type === 'date' || field.type === 'datetime-local') {
        payload[field.key] = value || null;
        return;
      }

      if (typeof value === 'string') {
        value = value.trim();
      }

      payload[field.key] = value === '' ? null : value;
    });

    return payload;
  }

  private getFieldOptions(field: ResourceFieldConfig): Observable<SelectOption[]> {
    if (field.options) {
      return of(field.options);
    }

    switch (field.optionResource) {
      case 'customers':
        return this.customerService.findAll().pipe(
          map((items) => items.map((item) => ({ value: item.id || 0, label: `${item.fullName} (${item.documentNumber})` })))
        );
      case 'species':
        return this.speciesService.findAll().pipe(
          map((items) => items.map((item) => ({ value: item.id || 0, label: item.name })))
        );
      case 'breeds':
        return this.breedService.findAll().pipe(
          map((items) => items.map((item) => ({ value: item.id || 0, label: item.speciesName ? `${item.name} (${item.speciesName})` : item.name })))
        );
      case 'pets':
        return this.petService.findAll().pipe(
          map((items) => items.map((item) => ({ value: item.id || 0, label: item.customerFullName ? `${item.name} - ${item.customerFullName}` : item.name })))
        );
      case 'veterinarians':
        return this.veterinarianService.findAll().pipe(
          map((items) => items.map((item) => ({ value: item.id || 0, label: item.specialty ? `${item.fullName} - ${item.specialty}` : item.fullName })))
        );
      case 'appointments':
        return this.appointmentService.findAll().pipe(
          map((items) => items.map((item) => ({ value: item.id || 0, label: `${item.petName || 'Appointment'} - ${this.formatDateTime(item.appointmentDateTime)}` })))
        );
      case 'medical-records':
        return this.medicalRecordService.findAll().pipe(
          map((items) => items.map((item) => ({ value: item.id || 0, label: `#${item.id || item.appointmentId} - ${item.diagnosis}` })))
        );
      default:
        return of([]);
    }
  }

  private formatDateTime(value?: string): string {
    if (!value) {
      return '';
    }

    return new Date(value).toLocaleString('es-CO', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
