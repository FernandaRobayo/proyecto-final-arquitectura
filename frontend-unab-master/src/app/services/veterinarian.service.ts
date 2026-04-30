import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Veterinarian } from '../models/veterinarian.model';
import { BaseResourceService } from './base-resource.service';

@Injectable({
  providedIn: 'root'
})
export class VeterinarianService extends BaseResourceService<Veterinarian> {
  constructor(http: HttpClient) {
    super(http, '/api/veterinarians');
  }
}
