import { Component, OnInit } from '@angular/core';
import { usuarios } from './usuario.model';
import { UsuariosService } from './usuarios.service';


@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.css']
})
export class UsuariosComponent implements OnInit {

  users: usuarios[] = [];

  constructor(private service: UsuariosService) { }

  ngOnInit(): void 
  {
    this.findAll();
  }


  displayedColumns: string[] = ['usuarioid','usuarionome','usuariocpf','usuariorg','acao'];

  findAll()
  {
    this.service.findAll().subscribe(resposta => 
    { 
      console.log(resposta);
      this.users = resposta; 
    })
  }
}
