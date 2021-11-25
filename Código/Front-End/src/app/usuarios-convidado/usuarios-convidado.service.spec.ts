import { TestBed } from '@angular/core/testing';

import { UsuariosConvidadoService } from './usuarios-convidado.service';

describe('UsuariosConvidadoService', () => {
  let service: UsuariosConvidadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsuariosConvidadoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
