import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { JwtHelperService } from "@auth0/angular-jwt";

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  isUserLoggedIn() {
    throw new Error('Method not implemented.');
  }

  public host: string = environment.apiUrl;
  private token: string = '';
  private loggedInEmail!: string;
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) {}

  public login(user: User): Observable<HttpResponse<User>> {
    return this.http.post<User>(`${this.host}/user/login`, user, { observe: 'response' });
  }

  public register(user: User): Observable<User> {

    return this.http.post<User>
    (`${this.host}/user/register`, user);
  }

  public logOut(): void {

    this.token = '';
    this.loggedInEmail = '';
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('users');
  }

  public saveToken(token: string): void {

    this.token = token;
    localStorage.setItem('token', token);
  }

  public addUserToLocalCache(user: User): void { localStorage.setItem('user', JSON.stringify(user)); }

  public getUserFromLocalCache(): User { return JSON.parse(localStorage.getItem('user')!); }

  public loadToken(): void { this.token = localStorage.getItem('token')!; }

  public getToken(): string { return this.token; }

  public isLoggedIn(): boolean {

    this.loadToken();
    if(this.token != null && this.token !=='') {
      if(this.jwtHelper.decodeToken(this.token).sub != null || '') {
        if(!this.jwtHelper.isTokenExpired(this.token)) {
          this.loggedInEmail = this.jwtHelper.decodeToken(this.token).sub;
          return true;
        }
      }
    }
    this.logOut();
    return false;
   }


}
