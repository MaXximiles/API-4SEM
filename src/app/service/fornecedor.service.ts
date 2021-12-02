import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Fornecedor } from '../model/fornecedor';

@Injectable({
  providedIn: 'root',
})
export class FornecedorService {
  host = environment.apiUrl;

  constructor(private http: HttpClient) {}

  public fetchAllFornecedores(): Observable<Fornecedor[]> {
    return this.http.get<Fornecedor[]>(`${this.host}/fornecedores/fetch/all`);
  }

  public fetchFornecedorById(id: number): Observable<Fornecedor> {
    return this.http.get<Fornecedor>(`${this.host}/fornecedores/fetch/${id}`);
  }

  public addFornecedor(formData: FormData): Observable<Fornecedor> {
    const object = this.formDataToObject(formData);
    return this.http.post<Fornecedor>(
      `${this.host}/fornecedores/add?descricao=${object['descricao']}&cnpj=${object['cnpj']}&email=${object['email']}&observacao=${object['observacao']}`,
      object
    );
  }

  public updateFornecedor(formData: FormData): Observable<Fornecedor> {
    const object = this.formDataToObject(formData);
    return this.http.put<Fornecedor>(
      `${this.host}/fornecedores/update/${formData.get('id')}?descricao=${
        object['descricao']
      }&cnpj=${object['cnpj']}&email=${object['email']}&observacao=${
        object['observacao']
      }`,
      object
    );
  }

  public deleteFornecedor(fornecedorId: number): Observable<Fornecedor> {
    return this.http.delete<Fornecedor>(
      `${this.host}/fornecedores/delete/${fornecedorId}`
    );
  }

  public createEventFormData(fornecedor: Fornecedor): FormData {
    const formData = new FormData();
    formData.append('id', fornecedor.id?.toString() || null);
    formData.append('descricao', fornecedor.descricao);
    formData.append('cnpj', fornecedor.cnpj);
    formData.append('email', fornecedor.email);
    formData.append('observacao', fornecedor.observacao);
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
