import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmailComposeComponent } from './email-compose.component';

describe('EmailComposeComponent', () => {
  let component: EmailComposeComponent;
  let fixture: ComponentFixture<EmailComposeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmailComposeComponent]
    });
    fixture = TestBed.createComponent(EmailComposeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
