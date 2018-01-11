import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ChannelManagePageComponent} from './channel-manage-page.component';

describe('ChannelManagePageComponent', () => {
  let component: ChannelManagePageComponent;
  let fixture: ComponentFixture<ChannelManagePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ChannelManagePageComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChannelManagePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
