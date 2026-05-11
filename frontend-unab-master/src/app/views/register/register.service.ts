import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../utils/entitys/user.entity';
import { buildApiUrl } from '../../utils/api-url';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  constructor(private http: HttpClient) { }

  registerUser(nombre: string, correo: string, username: string, password: string): Observable<User> {
    const data = {
      nombre,
      email: correo,
      username,
      password
    };

    return this.http.post<User>(buildApiUrl('/api/users'), JSON.stringify(data), { headers: this.headers });
  }
}
