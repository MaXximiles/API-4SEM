import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { AuthenticationGuard } from './guard/authentication.guard';
import { LoginComponent } from './login/login.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { UserComponent } from './user/user.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EventosComponent } from './eventos/eventos.component';
import { LoginGuard } from './guard/login.guard';
import { FornecedoresComponent } from './fornecedores/fornecedores.component';
import { RelatorioEventosComponent } from './relatorio-eventos/relatorio-eventos.component';
import { RelatorioVacinaComponent } from './relatorio-vacina/relatorio-vacina.component';
import { RelatoriColaboradoresComponent } from './relatorio-colaboradores/relatorio-colaboradores.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [LoginGuard] },
  {
    path: 'reset-password-front',
    component: ResetPasswordComponent,
    canActivate: [LoginGuard],
  },
  { path: 'register', component: RegisterComponent, canActivate: [LoginGuard] },
  {
    path: 'eventos',
    component: EventosComponent,
    canActivate: [AuthenticationGuard],
  },
  {
    path: 'user/management',
    component: UserComponent,
    canActivate: [AuthenticationGuard],
  },
  {
    path: 'user/profile',
    component: ProfileComponent,
    canActivate: [AuthenticationGuard],
  },
  { path: '', redirectTo: '/eventos', pathMatch: 'full' },
  {
    path: 'fornecedores',
    component: FornecedoresComponent,
    canActivate: [AuthenticationGuard],
  },
  {
    path: 'relatorio-eventos',
    component: RelatorioEventosComponent,
    canActivate: [AuthenticationGuard],
  },
  {
    path: 'relatorio-vacina',
    component: RelatorioVacinaComponent,
    canActivate: [AuthenticationGuard],
  },
  {
    path: 'relatorio-colaboradores',
    component: RelatoriColaboradoresComponent,
    canActivate: [AuthenticationGuard],
  },
  {
    path: 'change-password',
    component: ChangePasswordComponent,
    canActivate: [AuthenticationGuard],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
