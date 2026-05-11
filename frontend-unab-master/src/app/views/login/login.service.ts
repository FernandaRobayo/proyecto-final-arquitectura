import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap, timeout } from 'rxjs/operators';
import { AuthLoginRequest } from '../../models/auth-login-request.model';
import { AuthLoginResponse } from '../../models/auth-login-response.model';
import { buildApiUrl } from '../../utils/api-url';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private readonly tokenKey = 'veterinary.token';
  private readonly fullNameKey = 'veterinary.full_name';
  private readonly rolesKey = 'veterinary.roles';
  private readonly legacyTokenKey = 'token';
  private readonly legacyFullNameKey = 'full_name';
  private readonly legacyRolesKey = 'roles';

  constructor(private http: HttpClient) {
    this.normalizeStoredSession();
  }

  getApiUrl(): string {
    return buildApiUrl('/api');
  }

  login(payload: AuthLoginRequest): Observable<AuthLoginResponse> {
    return this.http.post<AuthLoginResponse>(buildApiUrl('/api/auth/login'), payload).pipe(
      timeout(5000),
      tap((response) => this.persistSession(response))
    );
  }

  logout(): void {
    this.clearSession();
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    const token = localStorage.getItem(this.tokenKey);

    if (!this.isSupportedToken(token)) {
      this.clearSession();
      return null;
    }

    return token;
  }

  getFullName(): string {
    return localStorage.getItem(this.fullNameKey) || '';
  }

  getRoles(): string[] {
    const rawRoles = localStorage.getItem(this.rolesKey);

    if (!rawRoles) {
      return [];
    }

    try {
      const parsedRoles = JSON.parse(rawRoles);
      return Array.isArray(parsedRoles) ? parsedRoles : [];
    } catch {
      this.clearSession();
      return [];
    }
  }

  private persistSession(response: AuthLoginResponse): void {
    this.clearLegacySession();
    localStorage.setItem(this.tokenKey, response.accessToken);
    localStorage.setItem(this.fullNameKey, response.fullName);
    localStorage.setItem(this.rolesKey, JSON.stringify(response.roles));
  }

  private normalizeStoredSession(): void {
    const legacyToken = localStorage.getItem(this.legacyTokenKey);
    const legacyFullName = localStorage.getItem(this.legacyFullNameKey);
    const legacyRoles = localStorage.getItem(this.legacyRolesKey);
    const namespacedToken = localStorage.getItem(this.tokenKey);

    if (!namespacedToken && legacyToken && this.isSupportedToken(legacyToken)) {
      localStorage.setItem(this.tokenKey, legacyToken);
      if (legacyFullName) {
        localStorage.setItem(this.fullNameKey, legacyFullName);
      }
      if (legacyRoles) {
        localStorage.setItem(this.rolesKey, legacyRoles);
      }
    }

    this.clearLegacySession();

    if (!this.isSupportedToken(localStorage.getItem(this.tokenKey))) {
      this.clearSession();
    }
  }

  private isSupportedToken(token: string | null): boolean {
    return !!token && token.startsWith('Basic ');
  }

  private clearSession(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.fullNameKey);
    localStorage.removeItem(this.rolesKey);
    this.clearLegacySession();
  }

  private clearLegacySession(): void {
    localStorage.removeItem(this.legacyTokenKey);
    localStorage.removeItem(this.legacyFullNameKey);
    localStorage.removeItem(this.legacyRolesKey);
  }
}
