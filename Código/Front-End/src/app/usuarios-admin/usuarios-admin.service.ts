import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { usuarios } from '../usuarios/usuario.model';
import { environment } from 'src/environments/environment';
import { UsuariosAdminModel } from './usuariosadmin.model';

@Injectable({
  providedIn: 'root'
})
export class UsuariosAdminService 
{

  baseUrl: String = environment.baseUrl;

  constructor(private http: HttpClient) { }

  insert(usuarios: UsuariosAdminModel): Observable<any>
  {
    const url = `${this.baseUrl}/user/add`;
    return this.http.post(url, usuarios);
  }
}
