import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormArray, Validators } from '@angular/forms';
import { environment } from 'src/environments/environment';
import { UsuariosColaboradorService } from './usuarios-colaborador.service';
import { UsuariosColaboradorModel } from './usuarioscolaborador.model';

@Component({
  selector: 'app-usuarios-colaborador',
  templateUrl: './usuarios-colaborador.component.html',
  styleUrls: ['./usuarios-colaborador.component.css']
})
export class UsuariosColaboradorComponent implements OnInit {

  formColaborador: any;
  baseUrl: String = environment.baseUrl;
  colaborador: UsuariosColaboradorModel = new UsuariosColaboradorModel();
  
  constructor(private http: HttpClient, private usuariosColaboradorService: UsuariosColaboradorService) { }

  salvarColaborador()
  {
    console.log(this.colaborador);
    this.usuariosColaboradorService.insert(this.colaborador).subscribe(colaborador => { colaborador = new UsuariosColaboradorModel})
  }

  ngOnInit(): void 
  {
    this.formColaborador = new FormGroup
    ({
      "colaborador_nome": new FormControl(),
      "colaborador_cpf": new FormControl(),
      "colaborador_rg": new FormControl(),
      "colaborador_funcao": new FormControl(),
      "colaborador_setor": new FormControl(),
      "colaborador_tipo": new FormControl(),
      "colaborador_email": new FormControl(),
      "colaborador_senha1": new FormControl(),
      "colaborador_senha2": new FormControl()
    })
  }

}