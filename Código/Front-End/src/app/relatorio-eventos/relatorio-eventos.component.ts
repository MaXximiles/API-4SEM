import { Component, OnInit } from '@angular/core';
import { stringify } from 'querystring';
import { Relatorio } from '../model/relatorio';
import { RelatoriosService } from '../service/relatorios.service';


@Component({
  selector: 'app-relatorio-eventos',
  templateUrl: './relatorio-eventos.component.html',
  styleUrls: ['./relatorio-eventos.component.css']
})
export class RelatorioEventosComponent implements OnInit {
  
  private relatorio: Relatorio[];

  dataIni: String = "";
  dataFim: String = "";

  constructor(private relatoriosService: RelatoriosService) { }

  ngOnInit(): void { }

  gerarRelatorio()
  {

    this.relatoriosService.relatorioEvento(this.dataIni,this.dataFim).subscribe((res: any) => 
    {
      const file = new Blob([res], 
      {
        type: res.type
      });
      
      const blob = window.URL.createObjectURL(file);

      const link = document.createElement('a');
      link.href = blob;
      link.download = 'relatorioEventos.pdf';
      link.click();

      window.URL.revokeObjectURL(blob);
      link.remove();
    });
  }
}
