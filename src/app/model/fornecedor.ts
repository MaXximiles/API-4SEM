export class Fornecedor {
  public id?: number;
  public descricao?: string;
  public cnpj?: string;
  public email?: string;
  public observacao?: string;

  constructor() {
    this.descricao = '';
    this.cnpj = '';
    this.email = '';
    this.observacao = '';
  }
}
