import { Component, OnInit } from '@angular/core';
import { RelatoriosService } from '../service/relatorios.service';

@Component({
  selector: 'app-relatorio-vacina',
  templateUrl: './relatorio-vacina.component.html',
  styleUrls: ['./relatorio-vacina.component.css']
})
export class RelatorioVacinaComponent implements OnInit {

  radioOption: string = "";

  constructor(private relatoriosService: RelatoriosService) { }

  ngOnInit(): void {
  }

  gerarRelatorio()
  {

    this.relatoriosService.relatorioVacina(this.radioOption).subscribe((res: any) => 
    {
      const file = new Blob([res], 
      {
        type: res.type
      });
      
      const blob = window.URL.createObjectURL(file);

      const link = document.createElement('a');
      link.href = blob;
      link.download = 'relatorioVacina.pdf';
      link.click();

      window.URL.revokeObjectURL(blob);
      link.remove();
    });
  }

}
