import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { environment } from 'src/environments/environment';
import { createIncrementalCompilerHost } from 'typescript';
import { Relatorio } from '../model/relatorio';
import { RelatoriosService } from '../service/relatorios.service';


@Component({
  selector: 'app-relatorio-colaboradores',
  templateUrl: './relatorio-colaboradores.component.html',
  styleUrls: ['./relatorio-colaboradores.component.css']
})

export class RelatoriColaboradoresComponent implements OnInit {

  filter: string = '';
  users: User[] = [];
  unfilteredUsers: User[] = [];
  invitedUser: User = null;
  caminho: string;
  link: Observable<string>;

  private host: string = environment.apiUrl;
  private relatorio: Relatorio[];


  constructor(
    private userService: UserService,
    private relatoriosService: RelatoriosService,
  ) { }

  filterUsers(filter): void {

    if (this.invitedUser != null && !`${this.invitedUser.firstName} ${this.invitedUser.lastName}` == filter ) {
      this.invitedUser = null;
    }

    if (!filter.length) {
      this.users = [];

      return;
    }

    this.users = this.unfilteredUsers.filter((unfilteredUser) =>
    {
      if (
        `${unfilteredUser.firstName} ${unfilteredUser.lastName}`.includes(
          filter
        ) ||
        unfilteredUser.email.includes(filter)
      ) {
        if (this.invitedUser == null) {
          return true;
        }

        if (this.invitedUser.id === unfilteredUser.id) {
          return false;
        }

        return true;
      }

      return false;
    });
  }

  onAddUser(addedUser: User): void {
    // If addedUser exists in the deletedUsers, remove it from the deletedUsers array. If it doesn't, add it to the addedUsers array and invitedUsers array


    this.invitedUser = addedUser; // Add to invitedUsers

    this.filter = `${this.invitedUser.firstName} ${this.invitedUser.lastName}`;

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

  gerarRelatorio()
  {
    let id = this.invitedUser != null ? this.invitedUser.id:0;

    this.relatoriosService.relatorioColaborador(id).subscribe((res: any) => {
      const file = new Blob([res], {
        type: res.type
      });
      
      const blob = window.URL.createObjectURL(file);

      const link = document.createElement('a');
      link.href = blob;
      link.download = 'relatorioColaborador.pdf';
      link.click();

      window.URL.revokeObjectURL(blob);
      link.remove();
    });
  }

}
