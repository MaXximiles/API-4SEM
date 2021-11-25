import { TestBed } from '@angular/core/testing';

import { UsuariosColaboradorService } from './usuarios-colaborador.service';

describe('UsuariosColaboradorService', () => {
  let service: UsuariosColaboradorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsuariosColaboradorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
