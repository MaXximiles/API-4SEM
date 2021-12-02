import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { usuarios } from './usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuariosService 
{
  baseUrl: String = environment.baseUrl;

  constructor(private http: HttpClient) { }

  findAll(): Observable<usuarios[]>
  {
    const url = `${this.baseUrl}/user/all`
    return this.http.get<usuarios[]>(url)
  }
}
