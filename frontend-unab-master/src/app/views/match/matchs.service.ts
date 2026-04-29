import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Match } from '../../utils/entitys/match.entity';
import { User } from '../../utils/entitys/user.entity';
import { environment } from '../../../environments/environment';

export interface MatchListItem {
  id: number;
  usuario: string;
  local: string;
  golesLocal: number;
  visitante: string;
  golesVisitante: number;
  fecha: string;
}

@Injectable({
  providedIn: 'root'
})
export class MatchsService {
  private apiBaseUrl = environment.apiBaseUrl;
  public urlBase = `${this.apiBaseUrl}/api/matchs`;

  constructor(public http: HttpClient) { }

  private get headers(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${localStorage.getItem('token') ?? ''}`
    });
  }

  public getMatchs(): Observable<MatchListItem[]> {
    return this.http.get<Array<[number, string, string, number, string, number, string]>>(`${this.urlBase}/all`, {
      headers: this.headers
    }).pipe(
      map((rows) => rows.map((row) => ({
        id: row[0],
        usuario: row[1],
        local: row[2],
        golesLocal: row[3],
        visitante: row[4],
        golesVisitante: row[5],
        fecha: row[6]
      })))
    );
  }

  public getMatchById(id: number | string): Observable<Match> {
    return this.http.get<Match>(`${this.urlBase}/match/${id}`, { headers: this.headers });
  }

  public saveMatch(id: number | string | null, data: unknown): Observable<Match> {
    if (id != null) {
      return this.http.put<Match>(`${this.urlBase}/${id}`, JSON.stringify(data), { headers: this.headers });
    }

    return this.http.post<Match>(this.urlBase, JSON.stringify(data), { headers: this.headers });
  }

  public deleteMatch(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.urlBase}/${id}`, { headers: this.headers });
  }

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiBaseUrl}/api/users`, { headers: this.headers });
  }
}
