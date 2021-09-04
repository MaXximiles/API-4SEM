import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { PoPageLogin } from '@po-ui/ng-templates';
import { Subscription } from 'rxjs';
import { AuthorizationService } from '../authorization/authorization.service';
import { UserLogin } from '../authorization/model/user-login';
import { MessagesService } from '../messages/messages.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  public isLoading: boolean = false;
  public logo: string = '';
  public secondaryLogo: string = '';
  private subs = new Subscription();

  constructor(
    private route: Router,
    private messageService: MessagesService,
    private authService: AuthorizationService
  ) {}

  ngOnInit(): void {}

  onloginSubmit(formData: PoPageLogin): void {
    const user: UserLogin = {
      userName: formData.login,
      password: formData.password,
      remindUser: formData.rememberUser,
    };
    this.login(user);
  }

  private login(user: UserLogin): void {
    this.isLoading = true;

    this.subs.add(
      this.authService.login(user).subscribe(
        () => {
          this.isLoading = false;
          const redirect = this.authService.redirectUrl
            ? this.authService.redirectUrl
            : '/home';
          this.route.navigate([redirect]);
        },
        (error: HttpErrorResponse) => {
          this.isLoading = false;
          this.messageService.showMessageError('Senha inválida');
          this.route.navigate(['/login']);
        }
      )
    );
  }
}
