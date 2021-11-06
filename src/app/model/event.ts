import { User } from './user';

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
  participantes: User[];
  maxParticipantes?: number;
  totalParticipantes?: number;
  user?: User;

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
    this.participantes = [];
    this.maxParticipantes = null;
    this.totalParticipantes = null;
    this.user = new User();
  }
}
