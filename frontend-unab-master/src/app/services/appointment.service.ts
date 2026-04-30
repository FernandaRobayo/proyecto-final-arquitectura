import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Appointment } from '../models/appointment.model';
import { BaseResourceService } from './base-resource.service';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService extends BaseResourceService<Appointment> {
  constructor(http: HttpClient) {
    super(http, '/api/appointments');
  }
}
