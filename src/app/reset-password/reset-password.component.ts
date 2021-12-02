import { NgForm } from '@angular/forms';
import { UserService } from './../service/user.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../service/authentication.service';
import { NotificationService } from '../service/notification.service';
import { Subscription } from 'rxjs/internal/Subscription';
import { CustomHttpResponse } from '../model/custom-http-response';
import { NotificationType } from '../enum/notification-type.enum';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  private subscriptions: Subscription[] = [];
  public showLoading: boolean = false;

  constructor(private router: Router, private authenticationService: AuthenticationService,
    private notificationService: NotificationService,
    private userService: UserService) { }

  ngOnInit(): void {

  }

  public onResetPasswordFront(emailForm: NgForm): void {
    this.showLoading = true;
    const cpf = emailForm.value['reset-password-cpf'];
    this.subscriptions.push(
      this.userService.resetUserPasswordFront(cpf).subscribe(
        (response: CustomHttpResponse) => {
          this.showLoading = false;
          this.sendNotification(NotificationType.SUCCESS,
            response.message);
        },
        (errorResponse: HttpErrorResponse) => {
          this.showLoading = false;
          this.sendNotification(NotificationType.WARNING, errorResponse.error.message);
        },
        ()=> emailForm.reset()
      )
    )
  }

  private sendNotification(notificationType: NotificationType, message: string): void {
    if (message) {
      this.notificationService.myNofity(notificationType, message);
    } else {
      this.notificationService.myNofity(notificationType, 'Um erro ocorreu. Por favor tente novamente');
    }
  }

}
