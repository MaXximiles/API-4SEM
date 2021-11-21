import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RelatoriColaboradoresComponent } from './relatorio-colaboradores.component';

describe('RelatoriColaboradoresComponent', () => {
  let component: RelatoriColaboradoresComponent;
  let fixture: ComponentFixture<RelatoriColaboradoresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RelatoriColaboradoresComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RelatoriColaboradoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
}); 