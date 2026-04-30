export interface Treatment {
  id?: number;
  medicalRecordId: number;
  description: string;
  medication?: string;
  dosage?: string;
  duration?: string;
  createdAt?: string;
}
