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
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  private subscriptions: Subscription[] = [];
  public showLoading: boolean = false;
  refreshing = false;
  email: string = "";
  senhaAntiga: string = "";
  senhaNova: string = "";

  constructor(private router: Router, private authenticationService: AuthenticationService,
    private notificationService: NotificationService,
    private userService: UserService) { }

  ngOnInit(): void {

  }

  onChangePassword(): void {

    this.refreshing = true;
    const formData = this.createEventFormData();
    this.subscriptions.push(

      this.userService.changePassword(formData).subscribe(
        (response: CustomHttpResponse) => {
          this.sendNotification(NotificationType.SUCCESS, response.message);
          this.refreshing = false;
        },
        (error: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR, error.error.message);
          this.refreshing = false;
        },
      )
    )
  }

  onEmail(s: string) {
    this.email = s;
  }
  onSenhaAntiga(s: string) {
    this.senhaAntiga = s;
  }
  onSenhaNova(s: string) {
    this.senhaNova = s;
  }

  public createEventFormData(): FormData {
    const formData2 = new FormData();
    formData2.append('email', this.email);
    formData2.append('senhaAntiga', this.senhaAntiga);
    formData2.append('senhaNova', this.senhaNova);
    return formData2;
  }

  private formDataToObject(formData: FormData): any {
    var object = {};

    formData.forEach(function (value, key) {
      object[key] = value;
    });

    return object;
  }

  private sendNotification(notificationType: NotificationType, message: string): void {
    if (message) {
      this.notificationService.myNofity(notificationType, message);
    } else {
      this.notificationService.myNofity(notificationType, 'Um erro ocorreu. Por favor tente novamente');
    }
  }

}
