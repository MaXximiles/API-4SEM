import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FornecedoresFormComponent } from './fornecedores-form.component';

describe('FornecedoresFormComponent', () => {
  let component: FornecedoresFormComponent;
  let fixture: ComponentFixture<FornecedoresFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FornecedoresFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FornecedoresFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
