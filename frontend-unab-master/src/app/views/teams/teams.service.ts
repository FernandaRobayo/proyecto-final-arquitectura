import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Team } from '../../utils/entitys/team.entity';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TeamsService {
  private apiBaseUrl = environment.apiBaseUrl;
  public urlBase = `${this.apiBaseUrl}/api/teams`;

  constructor(public http: HttpClient) { }

  private get headers(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${localStorage.getItem('token') ?? ''}`
    });
  }

  public getTeams(): Observable<Team[]> {
    return this.http.get<Team[]>(this.urlBase, { headers: this.headers });
  }

  public getTeamById(id: number | string): Observable<Team> {
    return this.http.get<Team>(`${this.urlBase}/${id}`, { headers: this.headers });
  }

  public saveTeam(id: number | string | null, data: Partial<Team>): Observable<Team> {
    if (id != null) {
      return this.http.put<Team>(`${this.urlBase}/${id}`, JSON.stringify(data), { headers: this.headers });
    }

    return this.http.post<Team>(this.urlBase, JSON.stringify(data), { headers: this.headers });
  }

  public deleteTeam(id: number | string): Observable<void> {
    return this.http.delete<void>(`${this.urlBase}/${id}`, { headers: this.headers });
  }
}
