import { TestBed } from '@angular/core/testing';
import { InventoryAnalytics } from './inventory-analytics';

describe('InventoryAnalytics', () => {
  let service: InventoryAnalytics;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InventoryAnalytics);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
