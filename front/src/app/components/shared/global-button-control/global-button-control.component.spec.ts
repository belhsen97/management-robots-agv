import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GlobalButtonControlComponent } from './global-button-control.component';

describe('GlobalButtonControlComponent', () => {
  let component: GlobalButtonControlComponent;
  let fixture: ComponentFixture<GlobalButtonControlComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GlobalButtonControlComponent]
    });
    fixture = TestBed.createComponent(GlobalButtonControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
