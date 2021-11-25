import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ModalComponent } from '../components/modal/modal.component';
import { ModalConfig } from '../components/modal/modal.config';
import { Fornecedor } from '../model/fornecedor';
import { FornecedorService } from '../service/fornecedor.service';

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

  modalConfig: ModalConfig = null;
  // Configurações do modal
  modalInsertConfig: ModalConfig = {
    modalTitle: 'Inserir um Evento',
    dismissButtonLabel: 'Confirmar Evento',
    closeButtonLabel: 'Fechar',
    onDismiss: () => {
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

  constructor(private fornecedorService: FornecedorService) {}

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
    });
  }

  onAddFornecedor(): void {
    this.openModalInsert();
  }

  onEditFornecedor(fornecedor: Fornecedor): void {
    this.openModalEdit(fornecedor);
  }

  onAddNewFornecedor(eventForm: NgForm): void {
    const formData = this.fornecedorService.createEventFormData(
      eventForm.value
    );
    this.fornecedorService.addFornecedor(formData).subscribe((fornecedor) => {
      this.fornecedores.push(fornecedor);
    });
  }

  onEditedFornecedor(eventForm: NgForm): void {
    const formData = this.fornecedorService.createEventFormData(
      eventForm.value
    );
    this.fornecedorService
      .updateFornecedor(formData)
      .subscribe((fornecedor) => {
        // atualizar fornecedor pelo id
        this.fornecedores.forEach((fornecedorAtual, index) => {
          if (fornecedorAtual.id === fornecedor.id) {
            this.fornecedores[index] = fornecedor;
          }
        });
      });
  }

  openModalInsert(): void {
    this.modalConfig = this.modalInsertConfig;
    this.modalComponent.open().then(() => {
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
}
