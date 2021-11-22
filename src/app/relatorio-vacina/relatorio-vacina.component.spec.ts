import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RelatorioVacinaComponent } from './relatorio-vacina.component';

describe('RelatorioVacinaComponent', () => {
  let component: RelatorioVacinaComponent;
  let fixture: ComponentFixture<RelatorioVacinaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RelatorioVacinaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RelatorioVacinaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
