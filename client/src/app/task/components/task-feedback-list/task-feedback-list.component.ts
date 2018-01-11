import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {Task} from '../../../shared/models/task';
import {TaskFeedback} from '../../../shared/models/task-feedback';

@Component({
  selector: 'jwjh-task-feedback-list',
  templateUrl: './task-feedback-list.component.html',
  styleUrls: ['./task-feedback-list.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskFeedbackListComponent {
  @Input()
  task: Task;
  @Input()
  taskFeedbacks: TaskFeedback[];
}
