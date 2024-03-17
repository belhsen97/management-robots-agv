import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsWorkstationComponent } from './details-workstation.component';

describe('DetailsWorkstationComponent', () => {
  let component: DetailsWorkstationComponent;
  let fixture: ComponentFixture<DetailsWorkstationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetailsWorkstationComponent]
    });
    fixture = TestBed.createComponent(DetailsWorkstationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
