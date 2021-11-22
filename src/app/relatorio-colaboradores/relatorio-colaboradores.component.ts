import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';


@Component({
  selector: 'app-relatorio-colaboradores',
  templateUrl: './relatorio-colaboradores.component.html',
  styleUrls: ['./relatorio-colaboradores.component.css']
})
export class RelatoriColaboradoresComponent implements OnInit {

  filter: string = '';
  users: User[] = [];
  unfilteredUsers: User[] = [];
  invitedUsers: User[] = [];
  deletedUsers: User[] = [];
  addedUsers: User[] = [];

  constructor(
    private userService: UserService,
  ) { }

  filterUsers(filter): void {
    if (!filter.length) {
      this.users = [];

      return;
    }

    this.users = this.unfilteredUsers.filter((unfilteredUser) => {
      if (
        `${unfilteredUser.firstName} ${unfilteredUser.lastName}`.includes(
          filter
        ) ||
        unfilteredUser.email.includes(filter)
      ) {
        if (this.invitedUsers.find((user) => user.id === unfilteredUser.id)) {
          return false;
        }

        return true;
      }

      return false;
    });
  }

  onAddUser(addedUser: User): void {
    // If addedUser exists in the deletedUsers, remove it from the deletedUsers array. If it doesn't, add it to the addedUsers array and invitedUsers array
    if (this.deletedUsers.find((user) => user.id === addedUser.id)) {
      this.deletedUsers = this.deletedUsers.filter(
        (user) => user.id !== addedUser.id
      ); // Remove from deletedUsers
    } else {
      this.addedUsers.push(addedUser); // Add to addedUsers
    }

    this.invitedUsers.push(addedUser); // Add to invitedUsers

    this.filterUsers(this.filter);
  }

  fetchUsers() {
    this.userService.fetchAllUsers().subscribe((users: User[]) => {
      this.unfilteredUsers = users;
    });
  }

  ngOnInit(): void {
    this.fetchUsers();
  }

}
