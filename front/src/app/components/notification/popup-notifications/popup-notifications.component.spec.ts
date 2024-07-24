import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PopupNotificationsComponent } from './popup-notifications.component';

describe('PopupNotificationsComponent', () => {
  let component: PopupNotificationsComponent;
  let fixture: ComponentFixture<PopupNotificationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PopupNotificationsComponent]
    });
    fixture = TestBed.createComponent(PopupNotificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
