import { TestBed } from '@angular/core/testing';

import { UsuariosAdminService } from './usuarios-admin.service';

describe('UsuariosAdminService', () => {
  let service: UsuariosAdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsuariosAdminService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
