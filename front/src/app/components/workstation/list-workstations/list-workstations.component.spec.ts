import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListWorkstationsComponent } from './list-workstations.component';

describe('ListWorkstationsComponent', () => {
  let component: ListWorkstationsComponent;
  let fixture: ComponentFixture<ListWorkstationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListWorkstationsComponent]
    });
    fixture = TestBed.createComponent(ListWorkstationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
