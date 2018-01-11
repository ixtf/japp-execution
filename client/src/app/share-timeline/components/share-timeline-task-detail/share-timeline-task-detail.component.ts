import {ChangeDetectionStrategy, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {MatDialog, MatTabChangeEvent} from '@angular/material';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import {Observable} from 'rxjs/Observable';
import {DefaultCompare} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskFeedback} from '../../../shared/models/task-feedback';
import {TaskOperatorContextData} from '../../../shared/models/task-operator-context-data';
import {shareTimelineTaskPageActions, shareTimelineTaskPageRightTabIndex} from '../../store';

@Component({
  selector: 'jwjh-share-timeline-task-detail',
  templateUrl: './share-timeline-task-detail.component.html',
  styleUrls: ['./share-timeline-task-detail.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ShareTimelineTaskDetailComponent implements OnInit, OnDestroy {
  @Input()
  shareOperator: Operator;
  taskFeedbacks: TaskFeedback[];
  @Input()
  taskOperatorContextDatas: TaskOperatorContextData[];
  rightTabIndex$: Observable<number>;
  private readonly subscriptions = [];

  constructor(private store: Store<any>,
              private dialog: MatDialog) {
    this.rightTabIndex$ = store.select(shareTimelineTaskPageRightTabIndex);
  }

  private _task: Task;

  @Input()
  get task(): Task {
    return this._task;
  }

  set task(value: Task) {
    this._task = value;
    this.taskFeedbacks = (value && value.taskFeedbacks || []).sort(DefaultCompare);
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

  ngOnInit() {
    setTimeout(() => {
      $('#mcover').css('display', 'none');
    }, 3000);
  }

  viewMore() {
    this.store.dispatch(new shareTimelineTaskPageActions.SetRightTabIndex(0));
  }

  focusChange(ev: MatTabChangeEvent) {
    this.store.dispatch(new shareTimelineTaskPageActions.SetRightTabIndex(ev.index));
  }

  nickname(operator: Operator): string {
    const taskOperatorContextData = this.taskOperatorContextDatas.find(it => it.operator.id === operator.id);
    return taskOperatorContextData.nickName || operator.name;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
