import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerChipsComponent } from './container-chips.component';

describe('ContainerChipsComponent', () => {
  let component: ContainerChipsComponent;
  let fixture: ComponentFixture<ContainerChipsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContainerChipsComponent]
    });
    fixture = TestBed.createComponent(ContainerChipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
