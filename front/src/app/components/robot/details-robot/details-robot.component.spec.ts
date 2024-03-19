import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsRobotComponent } from './details-robot.component';

describe('DetailsRobotComponent', () => {
  let component: DetailsRobotComponent;
  let fixture: ComponentFixture<DetailsRobotComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DetailsRobotComponent]
    });
    fixture = TestBed.createComponent(DetailsRobotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
