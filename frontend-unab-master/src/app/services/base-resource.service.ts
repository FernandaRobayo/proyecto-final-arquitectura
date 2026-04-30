import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resolveApiBaseUrl } from '../utils/api-url';

export interface CrudService<T> {
  findAll(): Observable<T[]>;
  findById(id: string | number): Observable<T>;
  create(payload: Partial<T>): Observable<T>;
  update(id: string | number, payload: Partial<T>): Observable<T>;
  delete(id: string | number): Observable<void>;
}

export abstract class BaseResourceService<T> implements CrudService<T> {
  constructor(protected http: HttpClient, private readonly endpoint: string) {}

  protected get apiUrl(): string {
    return resolveApiBaseUrl();
  }

  findAll(): Observable<T[]> {
    return this.http.get<T[]>(`${this.apiUrl}${this.endpoint}`);
  }

  findById(id: string | number): Observable<T> {
    return this.http.get<T>(`${this.apiUrl}${this.endpoint}/${id}`);
  }

  create(payload: Partial<T>): Observable<T> {
    return this.http.post<T>(`${this.apiUrl}${this.endpoint}`, payload);
  }

  update(id: string | number, payload: Partial<T>): Observable<T> {
    return this.http.put<T>(`${this.apiUrl}${this.endpoint}/${id}`, payload);
  }

  delete(id: string | number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}${this.endpoint}/${id}`);
  }
}
