import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsuariosAdminComponent } from './usuarios-admin/usuarios-admin.component';
import { FornecedoresComponent } from './fornecedores/fornecedores.component';
import { HomeComponent } from './home/home.component';
import { FornecedoresFormComponent } from './fornecedores-form/fornecedores-form.component';
import { UsuariosColaboradorComponent } from './usuarios-colaborador/usuarios-colaborador.component';
import { UsuariosConvidadoComponent } from './usuarios-convidado/usuarios-convidado.component';
import { UsuariosComponent } from './usuarios/usuarios.component';

const routes: Routes = [
  {path:"usuarios-admin/usuarios-admin.component", component:UsuariosAdminComponent},
  {path:"fornecedores/fornecedores.component", component:FornecedoresComponent},
  {path:"fornecedores-form/fornecedores-form.component", component:FornecedoresFormComponent},
  {path:"usuarios-colaborador/usuarios-colaborador.component", component:UsuariosColaboradorComponent},
  {path:"usuarios-convidado/usuarios-convidado.component", component:UsuariosConvidadoComponent},
  {path:"usuarios/usuarios.component", component:UsuariosComponent},
  {path:"", component:HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
