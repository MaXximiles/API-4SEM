import { HttpErrorResponse, HttpEvent } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotificationType } from 'src/app/enum/notification-type.enum';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { NotificationService } from 'src/app/service/notification.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  public user: User;
  public refreshing: boolean = false;
  private currentEmail: string;
  public profileImage!: File;
  private subscriptions: Subscription[] = [];
  public fileName: string = '';

  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.authenticationService.getUserFromLocalCache();
  }

  public onUpdateCurrentUser(user: User): void {
    this.refreshing = true;
    this.currentEmail =
      this.authenticationService.getUserFromLocalCache().email;
    const formData = this.userService.createUserFormData(
      this.currentEmail,
      user,
      this.profileImage
    );
    this.subscriptions.push(
      this.userService.updateCurrentUser(formData).subscribe(
        (response: User) => {
          this.authenticationService.addUserToLocalCache(response);
          this.fileName = null;
          this.profileImage = null;
          this.sendNotification(
            NotificationType.SUCCESS,
            `${response.firstName} ${response.lastName} foi atualizado com sucesso`
          );
          this.refreshing = false;
          this.user = this.authenticationService.getUserFromLocalCache();
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(
            NotificationType.ERROR,
            errorResponse.error.message
          );
          this.refreshing = false;
          this.profileImage = null;
        }
      )
    );
  }

  private sendNotification(
    notificationType: NotificationType,
    message: string
  ): void {
    if (message) {
      this.notificationService.myNofity(notificationType, message);
    } else {
      this.notificationService.myNofity(
        notificationType,
        'Um erro ocorreu. Por favor tente novamente'
      );
    }
  }

  public updateProfileImage(): void {
    this.clickButton('profile-image-input');
  }

  private clickButton(buttonId: string): void {
    document.getElementById(buttonId).click();
  }

  public onLogOut(): void {
    this.authenticationService.logOut();
    this.router.navigateByUrl('/login');
    this.sendNotification(
      NotificationType.SUCCESS,
      `Logout concluído com sucesso`
    );
  }

  public onProfileImageChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    const file = target.files as FileList;
    this.fileName = file[0].name;
    this.profileImage = file[0];
  }

  public onUpdateProfileImage(): void {
    const formData = new FormData();
    var flag = true;
    formData.append('email', this.user.email);
    formData.append('profileImage', this.profileImage);
    this.subscriptions.push(
      this.userService.updateProfileImage(formData).subscribe(
        (event: HttpEvent<any>) => {
          if (flag) {
            this.sendNotification(
              NotificationType.SUCCESS,
              `Foto de perfil atualizada com sucesso`
            );
            flag = false;
          }
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(
            NotificationType.ERROR,
            errorResponse.error.message
          );
        }
      )
    );
  }
}
