import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RelatorioEventosComponent } from './relatorio-eventos.component';

describe('RelatorioEventosComponent', () => {
  let component: RelatorioEventosComponent;
  let fixture: ComponentFixture<RelatorioEventosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RelatorioEventosComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RelatorioEventosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
