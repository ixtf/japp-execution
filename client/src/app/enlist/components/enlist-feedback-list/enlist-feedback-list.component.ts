import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {Enlist} from '../../../shared/models/enlist';
import {EnlistFeedback} from '../../../shared/models/enlist-feedback';

@Component({
  selector: 'jwjh-enlist-feedback-list',
  templateUrl: './enlist-feedback-list.component.html',
  styleUrls: ['./enlist-feedback-list.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EnlistFeedbackListComponent {
  @Input()
  enlist: Enlist;
  @Input()
  enlistFeedbacks: EnlistFeedback[];
}
