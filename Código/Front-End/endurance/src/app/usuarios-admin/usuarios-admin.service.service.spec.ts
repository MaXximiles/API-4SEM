import { TestBed } from '@angular/core/testing';

import { UsuariosAdmin.ServiceService } from './usuarios-admin.service.service';

describe('UsuariosAdmin.ServiceService', () => {
  let service: UsuariosAdmin.ServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsuariosAdmin.ServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
