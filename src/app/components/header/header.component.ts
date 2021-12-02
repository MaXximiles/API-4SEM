import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  public user$ = this.authenticationService.retornaUsuario();

  constructor(
    private authenticationService: AuthenticationService,
    public router: Router
  ) {}

  ngOnInit(): void {}
}
