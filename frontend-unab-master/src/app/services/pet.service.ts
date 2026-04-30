import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Pet } from '../models/pet.model';
import { BaseResourceService } from './base-resource.service';

@Injectable({
  providedIn: 'root'
})
export class PetService extends BaseResourceService<Pet> {
  constructor(http: HttpClient) {
    super(http, '/api/pets');
  }
}
