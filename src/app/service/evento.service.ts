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

  public addEvent(formData: FormData): Observable<Evento> {
    const object = this.formDataToObject(formData);
    const user: User = this.authService.getUserFromLocalCache();
    return this.http.post<Evento>(`${this.host}/events/add`, {
      ...object,
      status: 'PENDENTE',
      user: { ...user },
    });
  }

  public createEventFormData(evento: Evento): FormData {
    const formData = new FormData();
    formData.append(
      'inicio',
      moment(evento.inicio).format('YYYY-MM-DDTHH:mm:ss')
    );
    formData.append('fim', moment(evento.fim).format('YYYY-MM-DDTHH:mm:ss'));
    formData.append('local', evento.local);
    formData.append('tema', evento.tema);
    formData.append('descricao', evento.descricao);
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
