import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartsRobotsStatisticComponent } from './charts-robots-statistic.component';

describe('ChartsRobotsStatisticComponent', () => {
  let component: ChartsRobotsStatisticComponent;
  let fixture: ComponentFixture<ChartsRobotsStatisticComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChartsRobotsStatisticComponent]
    });
    fixture = TestBed.createComponent(ChartsRobotsStatisticComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
