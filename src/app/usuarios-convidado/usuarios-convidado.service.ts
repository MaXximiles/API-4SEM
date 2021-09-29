import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UsuariosAdminModel } from '../usuarios-admin/usuariosadmin.model';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsuariosConvidadoService {

  baseUrl: String = environment.baseUrl;

  constructor(private http: HttpClient) { }

  insert(convidado: UsuariosAdminModel): Observable<any> {
    const url = `${this.baseUrl}/user/add`;
    return this.http.post(url, convidado);
  }
}
