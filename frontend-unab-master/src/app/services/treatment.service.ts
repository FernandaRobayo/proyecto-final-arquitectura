import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Treatment } from '../models/treatment.model';
import { BaseResourceService } from './base-resource.service';

@Injectable({
  providedIn: 'root'
})
export class TreatmentService extends BaseResourceService<Treatment> {
  constructor(http: HttpClient) {
    super(http, '/api/treatments');
  }
}
