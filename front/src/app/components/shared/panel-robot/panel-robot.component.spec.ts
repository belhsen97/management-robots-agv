import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PanelRobotComponent } from './panel-robot.component';

describe('PanelRobotComponent', () => {
  let component: PanelRobotComponent;
  let fixture: ComponentFixture<PanelRobotComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PanelRobotComponent]
    });
    fixture = TestBed.createComponent(PanelRobotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
