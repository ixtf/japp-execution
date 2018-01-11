import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {Task} from '../../../shared/models/task';
import {TaskFeedback} from '../../../shared/models/task-feedback';
import {TaskFeedbackComment} from '../../../shared/models/task-feedback-comment';

@Component({
  selector: 'jwjh-task-feedback-comment-list',
  templateUrl: './task-feedback-comment-list.component.html',
  styleUrls: ['./task-feedback-comment-list.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskFeedbackCommentListComponent {
  @Input()
  task: Task;
  @Input()
  taskFeedback: TaskFeedback;
  @Input()
  taskFeedbackComments: TaskFeedbackComment[];
}
