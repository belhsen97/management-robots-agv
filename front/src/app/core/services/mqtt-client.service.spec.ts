import { TestBed } from '@angular/core/testing';

import { MqttClientService } from './mqtt-client.service';

describe('MqttClientService', () => {
  let service: MqttClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MqttClientService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
