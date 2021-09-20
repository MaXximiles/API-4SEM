import { NotificationType } from './../enum/notification-type.enum';
import { HttpErrorResponse } from '@angular/common/http';
import { User } from './../model/user';
import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Subscription } from 'rxjs';
import { UserService } from '../service/user.service';
import { NotificationService } from '../service/notification.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  // TODO mudar para eventos
  private titleSubject = new BehaviorSubject<string>('Users');
  private subscriptions: Subscription[] = [];
  public users: User[] = [];
  public refreshing: boolean = false;
  public selectedUser: User  = new User();
  public fileName: string = '';
  public profileImage!: File;
  public editUser = new User();
  private currentEmail: string;

  constructor(private userService: UserService,
    private notificationService: NotificationService) { }

  public fetchUsers(showNotification: boolean): void {
    // TODO mudar mensagem após cadastro de eventos pronto
    this.refreshing = true;
    this.subscriptions.push(
      this.userService.fetchAllUsers().subscribe(
        (response: User[]) => {
          this.userService.addUsersToLocalCache(response);
          this.users = response;
          console.log(this.users);
          console.log(this.users[0].active);
          this.refreshing = false;
          if(showNotification) {
            this.sendNotification(NotificationType.SUCCESS, `${response.length} usuário(s) cadastrados no evento`);
          }
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
          this.refreshing = false;
        }
      )
    )

  }

  public searchUsers(searchTerm: string): void {
    const results: User[] = [];

    for(const user of this.userService.fetchUsersFromLocalCache()) {
      const x = user.id.toString;
      if(user.firstName.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1 ||
         user.lastName.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1 ||
         user.email.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1) {
           results.push(user);
         }
    }
    this.users = results;
    if(results.length === 0 || !searchTerm) {
      this.users = this.userService.fetchUsersFromLocalCache();
    }
  }

  public onSelectUser(selectUser: User): void {
    this.selectedUser = selectUser;
    this.clickButton('openUserInfo');
    console.log(selectUser);
  }

  public onProfileImageChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    const file = (target.files as FileList);
    this.fileName = file[0].name;
    this.profileImage = file[0];
  }

  public saveNewUser(): void {
    this.clickButton('new-user-save');
  }

  public onAddNewUser(userForm: NgForm): void {
    const formData = this.userService.createUserFormData(null, userForm.value, this.profileImage);
    this.subscriptions.push(
      this.userService.addUser(formData).subscribe(
        (response: User) => {
          this.clickButton('new-user-close');
          this.fetchUsers(false);
          this.fileName = null;
          this.profileImage = null;
          userForm.reset();
          this.sendNotification(NotificationType.SUCCESS,
            `${response.firstName} ${response.lastName} foi atualizado com sucesso`)
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
          this.profileImage = null;
        }
      )

    );
  }

  public onEditUser(editUser: User): void {
    this.editUser = editUser;
    this.currentEmail = editUser.email;
    this.clickButton('openUserEdit');
  }

  private sendNotification(notificationType: NotificationType, message: string): void {
    if (message) {
      this.notificationService.myNofity(notificationType, message);
    } else {
      this.notificationService.myNofity(notificationType, 'Um erro ocorreu. Por favor tente novamente');
    }
  }

  private clickButton(buttonId: string): void {
    document.getElementById(buttonId).click();
  }
  ngOnInit(): void {
  }

}
