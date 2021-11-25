import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpEvent,
  HttpHeaderResponse,
  HttpHeaders,
  HttpResponse,
} from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { observable, Observable, of } from 'rxjs';
import { CustomHttpResponse } from '../model/custom-http-response';
import { createIncrementalCompilerHost } from 'typescript';
import { User } from '../model/user';
import { Relatorio } from '../model/relatorio';
import { Byte } from '@angular/compiler/src/util';


@Injectable({  providedIn: 'root'})

export class RelatoriosService {
  
  private host: string = environment.apiUrl;
  caminho: Observable<string>;

  constructor(private http: HttpClient) { }

  public relatorioColaborador(id: number)
  {
    return this.http.get(`${this.host}/relatorios/eventos_usuario/${id}`, {
      responseType: 'blob' as 'json'
    });
  }

  public relatorioEvento(dataini: String, datafim: String)
  {
      return this.http.get(`${this.host}/relatorios/eventos_periodo/${dataini},${datafim}`, {
      responseType: 'blob' as 'json'
    });
  }

  public relatorioVacina(vacinados: string)
  {
      return this.http.get(`${this.host}/relatorios/eventos_vacina/${vacinados}`, {
      responseType: 'blob' as 'json'
    });
  }
}
