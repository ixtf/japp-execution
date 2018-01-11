import {ChangeDetectionStrategy, Component} from '@angular/core';

@Component({
  selector: 'jwjh-channel-list',
  templateUrl: './channel-list.component.html',
  styleUrls: ['./channel-list.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ChannelListComponent {

  constructor() {
  }

}
