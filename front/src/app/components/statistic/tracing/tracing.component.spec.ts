import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TracingComponent } from './tracing.component';

describe('TracingComponent', () => {
  let component: TracingComponent;
  let fixture: ComponentFixture<TracingComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TracingComponent]
    });
    fixture = TestBed.createComponent(TracingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
