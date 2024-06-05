import { TestBed } from '@angular/core/testing';

import { MailClientService } from './mail-client.service';

describe('MailClientService', () => {
  let service: MailClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MailClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
