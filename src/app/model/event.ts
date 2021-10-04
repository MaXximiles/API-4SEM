export class Evento {
  id?: number;
  inicio?: Date;
  fim?: Date;
  local?: string;
  tema?: string;
  descricao?: string;
  observacao?: string;
  userEmail?: string;
  criacao?: Date;
  status?: string;
  maxParticipantes?: number;
  totalParticipantes?: number;

  constructor() {
    this.inicio = null;
    this.fim = null;
    this.local = '';
    this.tema = '';
    this.descricao = '';
    this.observacao = '';
    this.userEmail = '';
    this.criacao = null;
    this.status = '';
    this.maxParticipantes = null;
    this.totalParticipantes = null;
  }
}
