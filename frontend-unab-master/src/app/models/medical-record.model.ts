export interface MedicalRecord {
  id?: number;
  appointmentId: number;
  diagnosis: string;
  notes?: string;
  weight?: number;
  temperature?: number;
  createdAt?: string;
}
