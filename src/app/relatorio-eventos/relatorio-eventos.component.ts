import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-relatorio-eventos',
  templateUrl: './relatorio-eventos.component.html',
  styleUrls: ['./relatorio-eventos.component.css']
})
export class RelatorioEventosComponent implements OnInit {
  
  // AQUI COM  A VAR "doc" INDICAMOS O CAMINHO DO PDF
  doc = "C:/Users/lucas/Documents/MATEUS/FACULDADE/git%20atualizado/front/API-4SEM/src/app/relatorio-eventos/pdf.pdf";

  constructor() { }

  ngOnInit(): void {
    
  }


}
