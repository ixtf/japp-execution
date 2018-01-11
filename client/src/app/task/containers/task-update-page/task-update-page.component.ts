import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/pluck';
import {Observable} from 'rxjs/Observable';
import {Task} from '../../../shared/models/task';
import {TaskGroupService} from '../../../task-group/services/task-group.service';
import {TaskService} from '../../services/task.service';
import {taskUpdatePageActions} from '../../store';

@Component({
  selector: 'jwjh-task-update-page',
  templateUrl: './task-update-page.component.html',
  styleUrls: ['./task-update-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [TaskGroupService]
})
export class TaskUpdatePageComponent implements OnDestroy {
  title$: Observable<string>;
  task$: Observable<Task>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private route: ActivatedRoute,
              private taskGroupService: TaskGroupService,
              private taskService: TaskService) {
    this.title$ = route.queryParams.pluck('taskId')
      .map(id => id ? 'NAV.TASK-UPDATE' : 'NAV.TASK-CREATE');

    this.task$ = route.queryParams.switchMap((queryParams) => {
      if (queryParams.taskId) {
        return taskService.get(queryParams.taskId);
      }
      return taskGroupService.get(queryParams.taskGroupId).map(taskGroup => {
        return Task.assign({taskGroup});
      });
    });
  }

  save(task: Task) {
    this.store.dispatch(new taskUpdatePageActions.Save({task}));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
