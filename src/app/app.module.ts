import { AuthenticationGuard } from './guard/authentication.guard';
import { AuthenticationService } from './service/authentication.service';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserService } from './service/user.service';
import { AuthInterceptor } from './interceptor/auth.interceptor';
import { NotifierOptions } from 'angular-notifier/lib/models/notifier-config.model';
import { NotifierModule } from 'angular-notifier';
import { NotificationService } from './service/notification.service';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { UserComponent } from './user/user.component';
import { FormsModule } from '@angular/forms';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { NgxMaskModule, IConfig } from 'ngx-mask';
import { HeaderComponent } from './components/header/header.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EventosComponent } from './eventos/eventos.component';
import { FullCalendarComponent } from './components/full-calendar/full-calendar.component';
import { FullCalendarModule } from '@fullcalendar/angular';

import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';
import interactionPlugin from '@fullcalendar/interaction';
import { ModalComponent } from './components/modal/modal.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RelatorioEventosComponent } from './relatorio-eventos/relatorio-eventos.component';
import { RelatorioVacinaComponent } from './relatorio-vacina/relatorio-vacina.component';
import { RelatoriColaboradoresComponent } from './relatorio-colaboradores/relatorio-colaboradores.component';

import { PdfViewerComponent, PdfViewerModule } from 'ng2-pdf-viewer';
import { FornecedoresComponent } from './fornecedores/fornecedores.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

export const options: Partial<IConfig> | (() => Partial<IConfig>) = null;

const notifierCustomOptions: NotifierOptions = {
  position: {
    horizontal: {
      position: 'left',
      distance: 150,
    },
    vertical: {
      position: 'top',
      distance: 12,
      gap: 10,
    },
  },
  theme: 'material',
  behaviour: {
    autoHide: 5000,
    onClick: 'hide',
    onMouseover: 'pauseAutoHide',
    showDismissButton: true,
    stacking: 4,
  },
  animations: {
    enabled: true,
    show: {
      preset: 'slide',
      speed: 300,
      easing: 'ease',
    },
    hide: {
      preset: 'fade',
      speed: 300,
      easing: 'ease',
      offset: 50,
    },
    shift: {
      speed: 300,
      easing: 'ease',
    },
    overlap: 150,
  },
};

FullCalendarModule.registerPlugins([
  dayGridPlugin,
  timeGridPlugin,
  listPlugin,
  interactionPlugin,
]);

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    UserComponent,
    ResetPasswordComponent,
    HeaderComponent,
    ProfileComponent,
    EventosComponent,
    FornecedoresComponent,
    FullCalendarComponent,
    ModalComponent,
    RelatorioEventosComponent,
    RelatorioVacinaComponent,
    RelatoriColaboradoresComponent,
    ChangePasswordComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FullCalendarModule,
    NotifierModule.withConfig(notifierCustomOptions),
    FormsModule,
    NgxMaskModule.forRoot(),
    NgbModule,
    PdfViewerModule,
  ],
  providers: [
    AuthenticationService,
    UserService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    AuthenticationGuard,
    NotificationService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
