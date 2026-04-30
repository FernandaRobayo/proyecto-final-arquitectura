import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap, timeout } from 'rxjs/operators';
import { AuthLoginRequest } from '../../models/auth-login-request.model';
import { AuthLoginResponse } from '../../models/auth-login-response.model';
import { resolveApiBaseUrl } from '../../utils/api-url';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private readonly tokenKey = 'token';
  private readonly fullNameKey = 'full_name';
  private readonly rolesKey = 'roles';

  constructor(private http: HttpClient) { }

  getApiUrl(): string {
    return resolveApiBaseUrl();
  }

  login(payload: AuthLoginRequest): Observable<AuthLoginResponse> {
    return this.http.post<AuthLoginResponse>(`${this.getApiUrl()}/api/auth/login`, payload).pipe(
      timeout(5000),
      tap((response) => this.persistSession(response))
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.fullNameKey);
    localStorage.removeItem(this.rolesKey);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getFullName(): string {
    return localStorage.getItem(this.fullNameKey) || '';
  }

  getRoles(): string[] {
    const rawRoles = localStorage.getItem(this.rolesKey);
    return rawRoles ? JSON.parse(rawRoles) : [];
  }

  private persistSession(response: AuthLoginResponse): void {
    localStorage.setItem(this.tokenKey, response.accessToken);
    localStorage.setItem(this.fullNameKey, response.fullName);
    localStorage.setItem(this.rolesKey, JSON.stringify(response.roles));
  }
}
