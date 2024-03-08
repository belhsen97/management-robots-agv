import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddWorkstationComponent } from './add-workstation.component';

describe('AddWorkstationComponent', () => {
  let component: AddWorkstationComponent;
  let fixture: ComponentFixture<AddWorkstationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddWorkstationComponent]
    });
    fixture = TestBed.createComponent(AddWorkstationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
