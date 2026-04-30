import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Customer } from '../models/customer.model';
import { BaseResourceService } from './base-resource.service';

@Injectable({
  providedIn: 'root'
})
export class CustomerService extends BaseResourceService<Customer> {
  constructor(http: HttpClient) {
    super(http, '/api/customers');
  }
}
