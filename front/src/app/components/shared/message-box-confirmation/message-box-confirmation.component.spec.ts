import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageBoxConfirmationComponent } from './message-box-confirmation.component';

describe('MessageBoxConfirmationComponent', () => {
  let component: MessageBoxConfirmationComponent;
  let fixture: ComponentFixture<MessageBoxConfirmationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MessageBoxConfirmationComponent]
    });
    fixture = TestBed.createComponent(MessageBoxConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
