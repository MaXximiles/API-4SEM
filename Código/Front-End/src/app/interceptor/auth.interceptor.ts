import { AuthenticationService } from './../service/authentication.service';
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {}

  intercept(httpRequest: HttpRequest<any>, httpHandler: HttpHandler): Observable<HttpEvent<any>> {

    // TODO adicionar participar de evento
    if(httpRequest.url.includes(`${this.authenticationService.host}/user/login`)) {
      return httpHandler.handle(httpRequest);
    } if(httpRequest.url.includes(`${this.authenticationService.host}/user/register`)) {
      return httpHandler.handle(httpRequest);
    } if(httpRequest.url.includes(`${this.authenticationService.host}/user/reset-password`)) {
      return httpHandler.handle(httpRequest);
    }
    this.authenticationService.loadToken();
    const token = this.authenticationService.getToken();
    const request = httpRequest.clone({setHeaders: {Authorization: `Bearer ${token}`}});
    return httpHandler.handle(request);
  }
}
