import {ChangeDetectionStrategy, Component, Input, OnDestroy} from '@angular/core';
import {FormControl} from '@angular/forms';
import {MatDialog, MatTabChangeEvent} from '@angular/material';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import {Observable} from 'rxjs/Observable';
import {UtilService} from '../../../core/services/util.service';
import {WeixinService} from '../../../core/services/weixin.service';
import {coreAuthOperator} from '../../../core/store/index';
import {PickOperatorDialogComponent} from '../../../shared/components/pick-operator-dialog/pick-operator-dialog.component';
import {TaskComplainUpdateDialogComponent} from '../../../shared/components/task-complain-update-dialog/task-complain-update-dialog.component';
import {Task} from '../../../shared/models/task';
import {TaskEvaluate} from '../../../shared/models/task-evaluate';
import {TaskFeedback} from '../../../shared/models/task-feedback';
import {TaskFeedbackComment} from '../../../shared/models/task-feedback-comment';
import {MyTaskGroup} from '../../../shared/models/task-group';
import {OperatorNicknamePipe} from '../../../shared/services/operator-nickname.pipe';
import {TaskEvaluateUpdateDialogComponent} from '../../components/task-evaluate-update-dialog/task-evaluate-update-dialog.component';
import {TaskFeedbackCommentUpdateDialogComponent} from '../../components/task-feedback-comment-update-dialog/task-feedback-comment-update-dialog.component';
import {TaskFeedbackUpdateDialogComponent} from '../../components/task-feedback-update-dialog/task-feedback-update-dialog.component';
import {TaskNoticeUpdateDialogComponent} from '../../components/task-notice-update-dialog/task-notice-update-dialog.component';
import {TaskOperatorDialogComponent} from '../../components/task-operator-dialog/task-operator-dialog.component';
import {TaskOperatorImportDialogComponent} from '../../components/task-operator-import-dialog/task-operator-import-dialog.component';
import {TaskUnfeedbackerDialogComponent} from '../../components/task-unfeedbacker-dialog/task-unfeedbacker-dialog.component';
import {TaskBatchService} from '../../services/task-batch.service';
import {TaskService} from '../../services/task.service';
import {
  taskActions,
  taskEvaluateActions,
  taskFeedbackActions,
  taskFeedbackCommentActions,
  taskProgressPageActions,
  taskProgressPageStateRightTabIndex,
  taskProgressPageTaskEvaluates,
  taskProgressPageTaskFeedbacks,
  taskProgressPageTaskGroup,
} from '../../store';

@Component({
  selector: 'jwjh-task-progress-right',
  templateUrl: './task-progress-right.component.html',
  styleUrls: ['./task-progress-right.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskProgressRightComponent implements OnDestroy {
  feedbackFilterNameQCtrl = new FormControl();
  evaluateFilterNameQCtrl = new FormControl();
  @Input()
  task: Task;
  taskFeedbacks$: Observable<TaskFeedback[]>;
  taskEvaluates$: Observable<TaskEvaluate[]>;
  rightTabIndex$: Observable<number>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private taskBatchService: TaskBatchService,
              private router: Router,
              private dialog: MatDialog,
              private weixinService: WeixinService,
              private utilService: UtilService,
              private taskService: TaskService) {
    this.taskFeedbacks$ = store.select(taskProgressPageTaskFeedbacks);
    this.taskEvaluates$ = store.select(taskProgressPageTaskEvaluates);
    this.rightTabIndex$ = store.select(taskProgressPageStateRightTabIndex);

    this.subscriptions.push(
      this.feedbackFilterNameQCtrl.valueChanges
        .debounceTime(300)
        .distinctUntilChanged()
        .subscribe(q => {
          this.store.dispatch(new taskProgressPageActions.SetFeedbackFilterNameQ(q));
        }),
      this.evaluateFilterNameQCtrl.valueChanges
        .debounceTime(300)
        .distinctUntilChanged()
        .subscribe(q => {
          this.store.dispatch(new taskProgressPageActions.SetEvaluateFilterNameQ(q));
        })
    );
  }

  get isManager$(): Observable<boolean> {
    return this.taskService.isManager(this.task);
  }

  get isShowViewMore$() {
    return this.rightTabIndex$.map(i => {
      if (i === 0) {
        return false;
      }
      if (!this.task.content) {
        return false;
      }
      return true;
    });
  }

  get isShowShareTaskClock$() {
    return this.store.select(taskProgressPageTaskGroup)
      .map(it => (it && it.id) !== MyTaskGroup.id);
  }

  focusChange(ev: MatTabChangeEvent) {
    this.store.dispatch(new taskProgressPageActions.SetRightTabIndex(ev.index));
  }

  viewMore() {
    this.store.dispatch(new taskProgressPageActions.SetRightTabIndex(0));
  }

  deleteTaskFeedback(taskFeedback: TaskFeedback) {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new taskFeedbackActions.Delete(this.task.id, taskFeedback.id)));
  }

  updateTaskFeedbackComment(taskFeedback: TaskFeedback) {
    TaskFeedbackCommentUpdateDialogComponent.open(this.dialog, {task: this.task, taskFeedback});
  }

  deleteTaskFeedbackComment(taskFeedback: TaskFeedback, taskFeedbackComment: TaskFeedbackComment) {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new taskFeedbackCommentActions.Delete(this.task.id, taskFeedback.id, taskFeedbackComment.id)));
  }

  showParticipants(task: Task) {
    TaskOperatorDialogComponent.showParticipants(this.dialog, {task: this.task});
  }

  showFollowers(task: Task) {
    TaskOperatorDialogComponent.showFollowers(this.dialog, {task: this.task});
  }

  showUnFeedbackers() {
    this.dialog.open(TaskUnfeedbackerDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {task: this.task}
    });
  }

  showUnEvaluaters() {
  }

  feedbackFilterNoComment(b?: boolean) {
    this.store.dispatch(new taskProgressPageActions.SetFeedbackFilterNoComment(b));
  }

  addOperators() {
    TaskOperatorImportDialogComponent.open(this.dialog, {task: this.task});
  }

  top() {
    this.store.dispatch(new taskActions.Top(this.task));
  }

  copy() {
    this.store.dispatch(new taskActions.Copy(this.task.id));
  }

  notice() {
    TaskNoticeUpdateDialogComponent.open(this.dialog, {task: this.task});
  }

  move() {
    // this.store.dispatch(new taskProgressPageActions.ShowMoveDialog({task: this.task}));
  }

  done() {
    this.utilService.showConfirm().subscribe(() => this.store.dispatch(new taskActions.Done(this.task.id)));
  }

  updateNickname() {
    const nickName = prompt('');
    let key;
    if (nickName) {
      this.store.select(coreAuthOperator).take(1)
        .map(it => {
          key = OperatorNicknamePipe.key(it, this.task);
          const result = {};
          result[it.id] = nickName;
          return result;
        })
        .switchMap((res: any) => this.taskBatchService.updateNickNames([this.task], res))
        .subscribe(() => {
          sessionStorage.removeItem(key);
          this.store.dispatch(new taskActions.GetSuccess(this.task));
        });
    }
  }

  createTaskComplain() {
    TaskComplainUpdateDialogComponent.create(this.dialog, {task: this.task});
  }

  quit() {
    this.utilService.showConfirm().subscribe(() => this.store.dispatch(new taskActions.Quit(this.task.id)));
  }

  updateTaskFeedback(taskFeedback?: TaskFeedback) {
    TaskFeedbackUpdateDialogComponent.open(this.dialog, {task: this.task, taskFeedback});
  }

  updateTaskEvaluate(taskEvaluate?: TaskEvaluate) {
    PickOperatorDialogComponent.single(this.dialog, {source: this.task.participants})
      .afterClosed()
      .filter(it => it)
      .switchMap(executor => {
        return TaskEvaluateUpdateDialogComponent.open(this.dialog, {
          task: this.task,
          executor,
          taskEvaluate
        }).afterClosed();
      })
      .filter(it => it)
      .subscribe((res: TaskEvaluate) => {
        this.store.dispatch(new taskEvaluateActions.Save(this.task.id, res));
      });
  }

  goAdUrl() {
    this.router.navigateByUrl('https://mp.weixin.qq.com/s/3wzm1p_h3IPSkazKy8oYqw');
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
