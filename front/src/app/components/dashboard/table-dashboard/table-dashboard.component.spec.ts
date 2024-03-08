import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableDashboardComponent } from './table-dashboard.component';

describe('TableDashboardComponent', () => {
  let component: TableDashboardComponent;
  let fixture: ComponentFixture<TableDashboardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TableDashboardComponent]
    });
    fixture = TestBed.createComponent(TableDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
