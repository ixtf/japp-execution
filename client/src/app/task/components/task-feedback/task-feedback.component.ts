import {ChangeDetectionStrategy, Component, HostBinding, Input} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Store} from '@ngrx/store';
import {TranslateService} from '@ngx-translate/core';
import {Observable} from 'rxjs/Observable';
import {UtilService} from '../../../core/services/util.service';
import {coreAuthOperator} from '../../../core/store/index';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskFeedback} from '../../../shared/models/task-feedback';
import {TaskService} from '../../services/task.service';
import {taskFeedbackActions} from '../../store';
import {TaskFeedbackCommentUpdateDialogComponent} from '../task-feedback-comment-update-dialog/task-feedback-comment-update-dialog.component';
import {TaskFeedbackUpdateDialogComponent} from '../task-feedback-update-dialog/task-feedback-update-dialog.component';

@Component({
  selector: 'jwjh-task-feedback',
  templateUrl: './task-feedback.component.html',
  styleUrls: ['./task-feedback.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskFeedbackComponent {
  @HostBinding('class.jwjh-task-feedback') b1 = true;
  @Input()
  task: Task;
  @Input()
  taskFeedback: TaskFeedback;
  private authOperator$: Observable<Operator>;

  constructor(private store: Store<any>,
              private dialog: MatDialog,
              private translate: TranslateService,
              private utilService: UtilService,
              private taskService: TaskService) {
    this.authOperator$ = this.store.select(coreAuthOperator);
  }

  get isShowOnEdit(): boolean {
    const taskFeedbackComments = this.taskFeedback.taskFeedbackComments || [];
    return taskFeedbackComments.length === 0;
  }

  get isShowComment$(): Observable<boolean> {
    return this.taskService.isManager(this.task);
  }

  get isShowDelete$(): Observable<boolean> {
    return this.authOperator$.take(1).map(it => it.id === this.taskFeedback.creator.id);
  }

  edit() {
    TaskFeedbackUpdateDialogComponent.open(this.dialog, {task: this.task, taskFeedback: this.taskFeedback});
  }

  comment() {
    TaskFeedbackCommentUpdateDialogComponent.open(this.dialog, {task: this.task, taskFeedback: this.taskFeedback});
  }

  delete() {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new taskFeedbackActions.Delete(this.task.id, this.taskFeedback.id)));
  }

}
