import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RangeDateComponent } from './range-date.component';

describe('RangeDateComponent', () => {
  let component: RangeDateComponent;
  let fixture: ComponentFixture<RangeDateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RangeDateComponent]
    });
    fixture = TestBed.createComponent(RangeDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
