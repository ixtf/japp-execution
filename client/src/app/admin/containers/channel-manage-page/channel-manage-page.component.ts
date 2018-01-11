import {ChangeDetectionStrategy, Component} from '@angular/core';


@Component({
  selector: 'jwjh-channel-manage-page',
  templateUrl: './channel-manage-page.component.html',
  styleUrls: ['./channel-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ChannelManagePageComponent {

  constructor() {
  }

}
