import { NgForm } from '@angular/forms';
import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpEvent,
  HttpResponse,
} from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { CustomHttpResponse } from '../model/custom-http-response';

@Injectable({ providedIn: 'root' })
export class UserService {
  private host: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  public fetchAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.host}/user/all`);
  }

  public addUser(formData: FormData): Observable<User> {
    return this.http.post<User>(`${this.host}/user/add`, formData);
  }

  public updateUser(formData: FormData, userEmail: String): Observable<User> {
    return this.http.post<User>(
      `${this.host}/user/update/${userEmail}`,
      formData
    );
  }

  public updateCurrentUser(formData: FormData): Observable<User> {
    return this.http.post<User>(`${this.host}/user/update-me`, formData);
  }

  public resetUserPasswordFront(cpf: string): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      `${this.host}/user/reset-password-front/${cpf}`
    );
  }

  public changePassword(formData: FormData): Observable<any> {
    const object = this.formDataToObject(formData);

    return this.http.post<any>(
      `${this.host}/user/reset-password/?${object['email']}&${object['senhaAntiga']}&${object['senhaNova']}`, formData);
  }



  public updateProfileImage(formData: FormData): Observable<HttpEvent<User>> {
    return this.http.post<User>(
      `${this.host}/user/update-profile-image`,
      formData,
      {
        reportProgress: true,
        observe: 'events',
      }
    );
  }


  /*const object = this.formDataToObject(formData);
    return this.http.put<Fornecedor>(
      `${this.host}/fornecedores/update/${formData.get('id')}?descricao=${
        object['descricao']
      }&cnpj=${object['cnpj']}&email=${object['email']}&observacao=${
        object['observacao']
      }`,
      object
    );*/
  private formDataToObject(formData: FormData): any {
    var object = {};

    formData.forEach(function (value, key) {
      object[key] = value;
    });

    return object;
  }

  public updateVaccineImage(formData: FormData): Observable<HttpEvent<User>> {
    return this.http.post<User>(
      `${this.host}/user/update-vaccine-image`,
      formData,
      {
        reportProgress: true,
        observe: 'events',
      }
    );
  }

  public deleteUser(id: number): Observable<CustomHttpResponse> {
    return this.http.delete<CustomHttpResponse>(
      `${this.host}/user/delete/${id}`
    );
  }

  public addUsersToLocalCache(users: User[]): void {
    localStorage.setItem('users', JSON.stringify(users));
  }

  public fetchUsersFromLocalCache(): User[] {
    if (localStorage.getItem('users')) {
      return JSON.parse(localStorage.getItem('users')!);
    }
    return [];
  }

  public createUserFormData(
    loggedInEmail: string,
    user: User,
    profileImage: File
  ): FormData {
    const formData = new FormData();
    formData.append('currentEmail', loggedInEmail);
    formData.append('firstName', user.firstName);
    formData.append('lastName', user.lastName);
    formData.append('email', user.email);
    formData.append('cpf', user.cpf);
    formData.append('role', user.role);
    formData.append('isActive', JSON.stringify(user.active));
    formData.append('isNonLocked', JSON.stringify(user.notLocked));
    formData.append('profileImage', profileImage);
    return formData;
  }
}
