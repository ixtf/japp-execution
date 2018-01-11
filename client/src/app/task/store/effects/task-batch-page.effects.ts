import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Actions, Effect} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {DefaultCompare} from '../../../core/services/util.service';
import {ShowError, ShowWxQrcoode} from '../../../core/store/actions/core';
import {MyTaskGroup} from '../../../shared/models/task-group';
import {TaskBatchService} from '../../services/task-batch.service';
import {taskBatchPageActions} from '../../store';

@Injectable()
export class TaskBatchPageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(taskBatchPageActions.INIT)
    .exhaustMap((action: taskBatchPageActions.Init) => {
      let {taskGroupId} = action.cur;
      return this.taskBatchService.listTaskGroup().switchMap(taskGroups => {
        taskGroups = [MyTaskGroup].concat(taskGroups || []).sort(DefaultCompare);
        taskGroupId = taskGroupId || (taskGroups.length > 1 ? taskGroups[1].id : taskGroups[0].id);
        return this.taskBatchService.listTask(taskGroupId)
          .map(tasks => new taskBatchPageActions.InitSuccess(taskGroups, taskGroupId, tasks));
      }).catch(error => of(new ShowError(error)));
    });

  @Effect()
  tasksInvite$: Observable<Action> = this.actions$
    .ofType(taskBatchPageActions.TASKS_INVITE)
    .exhaustMap((action: taskBatchPageActions.TasksInvite) => {
      return this.taskBatchService.tasksInvite(action.tasks)
        .map(res => new ShowWxQrcoode(res.ticket, 'BUTTON.INVITE'))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  tasksFollowInvite$: Observable<Action> = this.actions$
    .ofType(taskBatchPageActions.TASKS_FOLLOW_INVITE)
    .exhaustMap((action: taskBatchPageActions.TasksFollowInvite) => {
      return this.taskBatchService.tasksFollowInvite(action.tasks)
        .map(res => new ShowWxQrcoode(res.ticket, 'BUTTON.FOLLOW_INVITE'))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions, private dialog: MatDialog, private taskBatchService: TaskBatchService) {
  }

}
