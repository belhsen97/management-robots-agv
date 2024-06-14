import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectButtonChoiceComponent } from './select-button-choice.component';

describe('SelectButtonChoiceComponent', () => {
  let component: SelectButtonChoiceComponent;
  let fixture: ComponentFixture<SelectButtonChoiceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SelectButtonChoiceComponent]
    });
    fixture = TestBed.createComponent(SelectButtonChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
