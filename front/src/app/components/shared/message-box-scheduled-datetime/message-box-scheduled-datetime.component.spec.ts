import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageBoxScheduledDatetimeComponent } from './message-box-scheduled-datetime.component';

describe('MessageBoxScheduledDatetimeComponent', () => {
  let component: MessageBoxScheduledDatetimeComponent;
  let fixture: ComponentFixture<MessageBoxScheduledDatetimeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MessageBoxScheduledDatetimeComponent]
    });
    fixture = TestBed.createComponent(MessageBoxScheduledDatetimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
