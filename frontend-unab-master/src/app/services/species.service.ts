import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Species } from '../models/species.model';
import { BaseResourceService } from './base-resource.service';

@Injectable({
  providedIn: 'root'
})
export class SpeciesService extends BaseResourceService<Species> {
  constructor(http: HttpClient) {
    super(http, '/api/species');
  }
}
