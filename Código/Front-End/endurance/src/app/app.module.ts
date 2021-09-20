import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UsuariosAdminComponent } from './usuarios-admin/usuarios-admin.component';
import { FornecedoresComponent } from './fornecedores/fornecedores.component';
import { HomeComponent } from './home/home.component';
import { FornecedoresFormComponent } from './fornecedores-form/fornecedores-form.component';
import { UsuariosColaboradorComponent } from './usuarios-colaborador/usuarios-colaborador.component';
import { UsuariosConvidadoComponent } from './usuarios-convidado/usuarios-convidado.component';
import { UsuariosComponent } from './usuarios/usuarios.component';
import { UsuariosColaboradorService } from './usuarios-colaborador/usuarios-colaborador.service';
import { UsuariosAdminService } from './usuarios-admin/usuarios-admin.service';
import { UsuariosConvidadoService } from './usuarios-convidado/usuarios-convidado.service';


@NgModule({
  declarations: [
    AppComponent,
    UsuariosAdminComponent,
    FornecedoresComponent,
    HomeComponent,
    FornecedoresFormComponent,
    UsuariosColaboradorComponent,
    UsuariosConvidadoComponent,
    UsuariosComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
