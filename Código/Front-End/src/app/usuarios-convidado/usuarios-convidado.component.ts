import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { UsuariosConvidadoService } from './usuarios-convidado.service';
import { UsuariosConvidadoModel } from './usuariosconvidado.model';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-usuarios-convidado',
  templateUrl: './usuarios-convidado.component.html',
  styleUrls: ['./usuarios-convidado.component.css']
})
export class UsuariosConvidadoComponent implements OnInit {

  formConvidado: any;
  baseUrl: String = environment.baseUrl;
  convidado: UsuariosConvidadoModel = new UsuariosConvidadoModel();

  constructor(private usuariosConvidadoService: UsuariosConvidadoService) { }
  
  salvaConvidado(){
    console.log(this.convidado);
    this.usuariosConvidadoService.insert(this.convidado).subscribe(convidado => {convidado = new UsuariosConvidadoModel})
  }

  ngOnInit(): void 
  {
    this.formConvidado = new FormGroup 
    ({
      "convidado_nome": new FormControl(),
      "convidado_cpf": new FormControl(),
      "convidado_rg": new FormControl(),
      "convidado_email": new FormControl(),
      "convidado_senha1": new FormControl(),
      "convidado_senha2": new FormControl(),
      "convidado_endereco": new FormControl(),
      "convidado_bairro": new FormControl(),
      "convidado_cep": new FormControl(),
      "convidado_cidade": new FormControl(),
      "convidado_estado": new FormControl(),
      "convidado_pais": new FormControl(),
    })
  }


}
