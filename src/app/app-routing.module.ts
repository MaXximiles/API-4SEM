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

const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [LoginGuard] },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'register', component: RegisterComponent, canActivate: [LoginGuard] },
  { path: 'eventos', component: EventosComponent },
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
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
