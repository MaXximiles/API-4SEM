import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsuariosConvidadoComponent } from './usuarios-convidado.component';

describe('UsuariosConvidadoComponent', () => {
  let component: UsuariosConvidadoComponent;
  let fixture: ComponentFixture<UsuariosConvidadoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsuariosConvidadoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UsuariosConvidadoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
