import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Evento } from '../model/event';
import { User } from '../model/user';
import { AuthenticationService } from './authentication.service';

import * as moment from 'moment';

@Injectable({
  providedIn: 'root',
})
export class EventoService {
  private host: string = environment.apiUrl;
  constructor(
    private http: HttpClient,
    private authService: AuthenticationService
  ) {}

  public fetchAllEvents(): Observable<Evento[]> {
    return this.http.get<Evento[]>(`${this.host}/events/all`);
  }

  public fetchEventById(id: number): Observable<Evento> {
    return this.http.get<Evento>(`${this.host}/events/fetch/${id}`);
  }

  public addEvent(formData: FormData): Observable<Evento> {
    const object = this.formDataToObject(formData);
    const user: User = this.authService.getUserFromLocalCache();
    return this.http.post<Evento>(`${this.host}/events/add`, {
      ...object,
      status: 'PENDENTE',
      user: { ...user },
    });
  }

  public updateEvent(formData: FormData): Observable<Evento> {
    const object = this.formDataToObject(formData);
    return this.http.put<Evento>(
      `${this.host}/events/update/${formData.get('id')}`,
      object
    );
  }

  public addGuest(user: User, id: number): Observable<Evento> {
    return this.http.put<Evento>(`${this.host}/events/add-guest/${id}`, user);
  }

  public removeGuest(user: User, id: number): Observable<Evento> {
    return this.http.put<Evento>(
      `${this.host}/events/remove-guest/${id}`,
      user
    );
  }

  public createEventFormData(evento: Evento): FormData {
    const formData = new FormData();
    formData.append('id', evento.id?.toString() || null);
    formData.append(
      'inicio',
      moment(evento.inicio).format('YYYY-MM-DDTHH:mm:ss')
    );
    formData.append('fim', moment(evento.fim).format('YYYY-MM-DDTHH:mm:ss'));
    formData.append('local', evento.local);
    formData.append('tema', evento.tema);
    formData.append('descricao', evento.descricao);
    formData.append('status', evento.status);
    return formData;
  }

  private formDataToObject(formData: FormData): any {
    var object = {};

    formData.forEach(function (value, key) {
      object[key] = value;
    });

    return object;
  }
}
