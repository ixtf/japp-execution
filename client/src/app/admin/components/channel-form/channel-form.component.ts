import {ChangeDetectionStrategy, Component} from '@angular/core';

@Component({
  selector: 'jwjh-channel-form',
  templateUrl: './channel-form.component.html',
  styleUrls: ['./channel-form.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ChannelFormComponent {

  constructor() {
  }

}
