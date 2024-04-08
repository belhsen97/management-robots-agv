import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GaugeChartComponent } from './gauge-chart.component';

describe('GaugeChartComponent', () => {
  let component: GaugeChartComponent;
  let fixture: ComponentFixture<GaugeChartComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GaugeChartComponent]
    });
    fixture = TestBed.createComponent(GaugeChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
