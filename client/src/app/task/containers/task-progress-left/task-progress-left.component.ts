import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {Task} from '../../../shared/models/task';
import {TaskFeedbackUpdateDialogComponent} from '../../components/task-feedback-update-dialog/task-feedback-update-dialog.component';
import {TaskService} from '../../services/task.service';
import {taskProgressPageTaskId, taskProgressPageTasks} from '../../store';

@Component({
  selector: 'jwjh-task-progress-left',
  templateUrl: './task-progress-left.component.html',
  styleUrls: ['./task-progress-left.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskProgressLeftComponent {
  tasks$: Observable<Task[]>;
  selectedId$: Observable<string>;

  constructor(private store: Store<any>,
              private router: Router,
              private dialog: MatDialog,
              private taskService: TaskService) {
    this.tasks$ = store.select(taskProgressPageTasks);
    this.selectedId$ = this.store.select(taskProgressPageTaskId);
  }

  selectTask(task: Task) {
    const taskGroupId = task.taskGroup.id;
    const taskId = task.id;
    this.router.navigate(['tasks', 'progress'], {queryParams: {taskGroupId, taskId}});
  }

  updateTaskFeedback(task: Task) {
    TaskFeedbackUpdateDialogComponent.open(this.dialog, {task});
  }

  isShowFeedbackBtn$(task: Task): Observable<boolean> {
    return this.taskService.isParticipant(task);
  }

  isShowFeedbackCount(task: Task): boolean {
    return task.taskContextData && task.taskContextData.taskFeedbackCount > 0;
  }

  isShowNeverRead(task: Task): boolean {
    return task.taskOperatorContextData && task.taskOperatorContextData.neverRead && task.isStarted;
  }

  isShowTaskFeedbackUnreadCount$(task: Task): Observable<boolean> {
    return this.taskService.isManager(task)
      .map(isManager => {
        return !this.isShowNeverRead(task) && isManager && task.isStarted
          && task.taskOperatorContextData && task.taskOperatorContextData.taskFeedbackUnreadCount > 0;
      });
  }

  isShowTaskFeedbackCommentUnreadCount$(task: Task): Observable<boolean> {
    return this.taskService.isManager(task)
      .map(isManager => {
        return !this.isShowNeverRead(task) && !isManager && task.isStarted
          && task.taskOperatorContextData && task.taskOperatorContextData.taskFeedbackCommentUnreadCount > 0;
      });
  }

}
