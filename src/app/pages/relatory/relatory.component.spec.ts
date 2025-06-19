import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RelatoryComponent } from './relatory.component';

describe('RelatoryComponent', () => {
  let component: RelatoryComponent;
  let fixture: ComponentFixture<RelatoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RelatoryComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RelatoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
