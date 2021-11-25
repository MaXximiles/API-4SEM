import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsuariosColaboradorComponent } from './usuarios-colaborador.component';

describe('UsuariosColaboradorComponent', () => {
  let component: UsuariosColaboradorComponent;
  let fixture: ComponentFixture<UsuariosColaboradorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsuariosColaboradorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UsuariosColaboradorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
