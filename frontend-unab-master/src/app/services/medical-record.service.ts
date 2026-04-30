import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MedicalRecord } from '../models/medical-record.model';
import { BaseResourceService } from './base-resource.service';

@Injectable({
  providedIn: 'root'
})
export class MedicalRecordService extends BaseResourceService<MedicalRecord> {
  constructor(http: HttpClient) {
    super(http, '/api/medical-records');
  }
}
