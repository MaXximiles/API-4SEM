import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { NotificationType } from '../enum/notification-type.enum';
import { NotificationService } from '../service/notification.service';
import { RelatoriosService } from '../service/relatorios.service';

@Component({
  selector: 'app-relatorio-vacina',
  templateUrl: './relatorio-vacina.component.html',
  styleUrls: ['./relatorio-vacina.component.css']
})
export class RelatorioVacinaComponent implements OnInit {

  radioOption: string = "";
  refreshing: boolean = false;
  subscriptions: Subscription[] = [];

  constructor(private relatoriosService: RelatoriosService,
    private notificationService: NotificationService) { }

  ngOnInit(): void {
  }

  gerarRelatorio()
  {
    this.refreshing = true;
    document.getElementById("relatorio-vacina-btn").setAttribute("disabled","disabled");

    this.subscriptions.push(
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
      this.refreshing = false;
      document.getElementById("relatorio-vacina-btn").removeAttribute("disabled");
    },
    (errorResponse: HttpErrorResponse) => {
      this.sendNotification(
        NotificationType.ERROR,
        errorResponse.error.message
      );
      this.refreshing = false;
      document.getElementById("relatorio-vacina-btn").removeAttribute("disabled");
    }
    )
    );
  }

  private sendNotification(
    notificationType: NotificationType,
    message: string
  ): void {
    if (message) {
      this.notificationService.myNofity(notificationType, message);
    } else {
      this.notificationService.myNofity(
        notificationType,
        'Um erro ocorreu. Por favor tente novamente'
      );
    }
  }
}
