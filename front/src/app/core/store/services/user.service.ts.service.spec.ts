import { TestBed } from '@angular/core/testing';

import { UserServiceTsService } from './user.service.ts.service';

describe('UserServiceTsService', () => {
  let service: UserServiceTsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserServiceTsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
