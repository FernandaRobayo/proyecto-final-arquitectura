import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Breed } from '../models/breed.model';
import { BaseResourceService } from './base-resource.service';

@Injectable({
  providedIn: 'root'
})
export class BreedService extends BaseResourceService<Breed> {
  constructor(http: HttpClient) {
    super(http, '/api/breeds');
  }
}
