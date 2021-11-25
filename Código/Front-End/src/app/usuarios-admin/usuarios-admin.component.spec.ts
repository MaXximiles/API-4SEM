import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsuariosAdminComponent } from './usuarios-admin.component';

describe('UsuariosAdminComponent', () => {
  let component: UsuariosAdminComponent;
  let fixture: ComponentFixture<UsuariosAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsuariosAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UsuariosAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
