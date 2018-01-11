import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {Actions} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {TaskGroup} from '../../../shared/models/task-group';
import {taskGroupActions, taskGroupManagePageActions, taskGroupManagePageTaskGroups} from '../../store';

@Component({
  selector: 'jwjh-task-group-manage-page',
  templateUrl: './task-group-manage-page.component.html',
  styleUrls: ['./task-group-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskGroupManagePageComponent implements OnDestroy {
  taskGroups$: Observable<TaskGroup[]>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private actions$: Actions) {
    this.taskGroups$ = this.store.select(taskGroupManagePageTaskGroups);
    store.dispatch(new taskGroupManagePageActions.Init());
  }

  top(taskGroup: TaskGroup) {
    this.store.dispatch(new taskGroupActions.Top(taskGroup));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
