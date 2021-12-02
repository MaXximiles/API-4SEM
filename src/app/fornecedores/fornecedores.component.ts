import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Subscription } from 'rxjs';
import { ModalComponent } from '../components/modal/modal.component';
import { ModalConfig } from '../components/modal/modal.config';
import { NotificationType } from '../enum/notification-type.enum';
import { Fornecedor } from '../model/fornecedor';
import { FornecedorService } from '../service/fornecedor.service';
import { NotificationService } from '../service/notification.service';

@Component({
  selector: 'app-fornecedores',
  templateUrl: './fornecedores.component.html',
  styleUrls: ['./fornecedores.component.css'],
})
export class FornecedoresComponent implements OnInit {
  @ViewChild('modalFornecedor') private modalComponent: ModalComponent;
  @ViewChild('fornecedorForm') private fornecedorForm: NgForm;

  public fornecedores: Fornecedor[] = [];
  public editFornecedor: Fornecedor = new Fornecedor();
  private subscriptions: Subscription[] = [];

  modalConfig: ModalConfig = null;
  // Configurações do modal
  modalInsertConfig: ModalConfig =
  {
    modalTitle: 'Fornecedores',
    dismissButtonLabel: 'Cadastrar Fornecedor',
    closeButtonLabel: 'Fechar',
    onDismiss: () =>
    {
      this.fornecedorForm.ngSubmit.emit();
      return true;
    },
  };
  modalEditConfig: ModalConfig = {
    modalTitle: `Editar ${this.editFornecedor.descricao}`,
    dismissButtonLabel: 'Salvar Alterações',
    closeButtonLabel: 'Fechar',
    onDismiss: () => {
      this.fornecedorForm.ngSubmit.emit();
      return true;
    },
  };

  constructor(private fornecedorService: FornecedorService, private notificationService: NotificationService,) {}

  ngOnInit(): void {
    this.fornecedorService.fetchAllFornecedores().subscribe((fornecedores) => {
      this.fornecedores = fornecedores;
    });
  }

  onSelectFornecedor(fornecedor: Fornecedor): void {
    console.log(fornecedor);
  }

  onDeleteFornecedor(fornecedor: Fornecedor): void {
    this.fornecedorService.deleteFornecedor(fornecedor.id).subscribe(() => {
      this.fornecedores = this.fornecedores.filter(
        (fornecedorAtual) => fornecedorAtual.id !== fornecedor.id

      );
      this.sendNotification(
        NotificationType.WARNING,
        `${fornecedor.descricao} removido com sucesso`
      )
    });
  }

  onAddFornecedor(): void
  {
    this.openModalInsert();
  }

  onEditFornecedor(fornecedor: Fornecedor): void {
    this.openModalEdit(fornecedor);
  }

  onAddNewFornecedor(eventForm: NgForm): void {
    const formData = this.fornecedorService.createEventFormData( eventForm.value);
    this.subscriptions.push(
      this.fornecedorService.addFornecedor(formData).subscribe(
        (response: Fornecedor) => {
          this.fornecedores.push(response);
          this.sendNotification(
            NotificationType.SUCCESS,
            `${response.descricao} foi adicionado com sucesso`
          );
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(
            NotificationType.ERROR,
            errorResponse.error.message
          );
        }
      )
    );

  }

  onEditedFornecedor(eventForm: NgForm): void
  {
    const formData = this.fornecedorService.createEventFormData(eventForm.value);
    this.subscriptions.push(
    this.fornecedorService
      .updateFornecedor(formData)
      .subscribe((fornecedor) =>
      {
        // atualizar fornecedor pelo id
        this.fornecedores.forEach((fornecedorAtual, index) =>
        {
          if (fornecedorAtual.id === fornecedor.id) {
            this.fornecedores[index] = fornecedor;
            this.sendNotification(
              NotificationType.SUCCESS,
              `${fornecedor.descricao} foi atualizado com sucesso`
            )
          }
        }
        )
      },
      (errorResponse: HttpErrorResponse) => {
        this.sendNotification(
          NotificationType.ERROR,
          errorResponse.error.message
        );
        }
      )
    );
  }

  openModalInsert(): void {
    this.modalConfig = this.modalInsertConfig;
    this.modalComponent.open().then(() =>
    {
      this.editFornecedor = new Fornecedor();
      this.fornecedorForm.resetForm();
    });
  }

  openModalEdit(fornecedor: Fornecedor): void {
    this.editFornecedor = fornecedor;
    this.modalConfig = this.modalEditConfig;
    this.modalComponent.open().then(() => {
      this.editFornecedor = new Fornecedor();
      this.fornecedorForm.resetForm();
    });
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
