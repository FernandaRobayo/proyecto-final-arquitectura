export interface Appointment {
  id?: number;
  petId: number;
  petName?: string;
  veterinarianId: number;
  veterinarianFullName?: string;
  appointmentDateTime: string;
  reason: string;
  status: string;
  createdAt?: string;
}
