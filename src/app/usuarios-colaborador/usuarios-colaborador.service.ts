import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UsuariosColaboradorModel } from './usuarioscolaborador.model';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuariosColaboradorService {

  baseUrl: String = environment.baseUrl;

  constructor(private http: HttpClient) { }

  insert(colaborador: UsuariosColaboradorModel): Observable<any>
  {
    const url = `${this.baseUrl}/user/add`;
    return this.http.post(url, colaborador);
  }
}
