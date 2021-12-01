import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormArray, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { usuarios } from '../usuarios/usuario.model';
import { UsuariosAdminService } from './usuarios-admin.service';
import { UsuariosAdminModel } from './usuariosadmin.model';


@Component({
  selector: 'app-usuarios-admin',
  templateUrl: './usuarios-admin.component.html',
  styleUrls: ['./usuarios-admin.component.css']
})
export class UsuariosAdminComponent implements OnInit 
{
  
  formAdmin: any;
  baseUrl: String = environment.baseUrl;
  usuarios: UsuariosAdminModel = new UsuariosAdminModel();
  
  constructor(private http: HttpClient, private service: UsuariosAdminService) 
  {   }

  
  // usuarios: usuarios =
  // {
  //     usuarioId: '',
  //     usuarioFirstName: '',
  //     usuarioCpf: '',
  //     usuarioRg: '',
  //     usuarioEmail: '',
  //     usuarioPassword: ''
  // };


  salvaAdmin()
  {
    console.log(this.usuarios);
    this.service.insert(this.usuarios).subscribe(usuarios => { usuarios = new UsuariosAdminModel})
  }

  // salvaAdmin(usuarios: usuarios)
  // { alert(JSON.stringify(usuarios, null, 4)); }

  ngOnInit(): void 
  {

    this.formAdmin = new FormGroup
    ({  
      "admin_nome": new FormControl(),
      "admin_cpf": new FormControl(),
      "admin_rg": new FormControl(),
      "admin_funcao": new FormControl(),
      "admin_setor": new FormControl(),
      "admin_email": new FormControl(),
      "admin_senha1": new FormControl(),
      "admin_senha2": new FormControl()
      });
  }

  // salvaAdmin(usuarios: usuarios)
  // {
  //   this.service.insert(this.usuarios).subscribe(() =>
  //   {
  //     alert('Success!');
  //   },
  //     () => { alert('Error'); });
  // }

  // salvaAdmin()
  // {
  //   const nome = this.formAdmin.get("admin_nome").value;
  //   const cpf = this.formAdmin.get("admin_cpf").value;
  //   const rg = this.formAdmin.get("admin_rg").value;
  //   const funcao = this.formAdmin.get("admin_funcao").value;
  //   const setor = this.formAdmin.get("admin_setor").value;
  //   const email= this.formAdmin.get("admin_email").value;
  //   const senha1 = this.formAdmin.get("admin_senha1").value;
  //   const senha2 = this.formAdmin.get("admin_senha2").value;
  //   console.log(nome, cpf, rg, funcao, setor, email, senha1, senha2)
  // }

}
