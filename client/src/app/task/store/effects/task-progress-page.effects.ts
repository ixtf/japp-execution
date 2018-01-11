import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/last';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {forkJoin} from 'rxjs/observable/forkJoin';
import {from} from 'rxjs/observable/from';
import {of} from 'rxjs/observable/of';
import {isNullOrUndefined} from 'util';
import {DefaultCompare, UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {coreIsMobile} from '../../../core/store/index';
import {Task} from '../../../shared/models/task';
import {MyTaskGroup, TaskGroup} from '../../../shared/models/task-group';
import {TaskGroupService} from '../../../task-group/services/task-group.service';
import {TaskService} from '../../services/task.service';
import {taskActions, taskEvaluateActions, taskFeedbackActions, taskProgressPageActions} from '../../store';

@Injectable()
export class TaskProgressPageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(taskProgressPageActions.INIT)
    .exhaustMap((action: taskProgressPageActions.Init) => {
      const {old, cur} = action;
      const {taskGroupId, taskId} = cur;
      const noTaskGroupId = isNullOrUndefined(taskGroupId);
      const noTaskId = isNullOrUndefined(taskId);
      let taskGroups$: Promise<TaskGroup[]> = Promise.resolve(null);
      let taskGroupId$: Promise<string> = Promise.resolve(taskGroupId);
      let tasks$: Promise<Task[]> = Promise.resolve(null);
      // 第一次进入
      const firstTime = () => {
        taskGroups$ = this.taskGroupService.list()
          .map(taskGroups => [MyTaskGroup].concat(taskGroups || []).sort(DefaultCompare))
          .toPromise();
        if (taskId) {
          taskGroupId$ = this.taskService.get(cur.taskId).map(it => it.taskGroup && it.taskGroup.id || MyTaskGroup.id).toPromise();
          tasks$ = taskGroupId$.then(it => this.taskGroupService.listTask(it).toPromise());
        } else {
          taskGroupId$ = taskGroups$.then(taskGroups => taskGroups.length > 1 ? taskGroups[1].id : taskGroups[0].id);
          tasks$ = taskGroupId$.then(it => this.taskGroupService.listTask(it).toPromise());
        }
      };
      if (isNullOrUndefined(old)) {
        firstTime();
      } else {
        if (noTaskId && noTaskGroupId) {
          firstTime();
        } else if (taskGroupId !== old.taskGroupId) {
          taskGroups$ = this.taskGroupService.list()
            .map(taskGroups => [MyTaskGroup].concat(taskGroups || []))
            .toPromise();
          tasks$ = this.taskGroupService.listTask(taskGroupId).toPromise();
        }
      }
      return forkJoin(taskGroups$, taskGroupId$, tasks$, this.store.select(coreIsMobile).take(1),
        (taskGroups, _taskGroupId, tasks, isMobile) => {
          let actions: Action[] = [new taskProgressPageActions.InitSuccess(taskGroups, _taskGroupId, tasks, taskId)];
          if (taskId) {
            actions = actions.concat([
              new taskFeedbackActions.List(taskId),
              new taskEvaluateActions.List(taskId),
              new taskActions.Read(taskId)
            ]);
          }
          (tasks || []).forEach(task => {
            actions = actions.concat([
              new taskActions.GetContextData(task.id),
              new taskActions.GetOperatorContextData(task.id),
            ]);
          });
          return actions;
        }).switchMap(actions => from(actions))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private dialog: MatDialog,
              private utilService: UtilService,
              private taskGroupService: TaskGroupService,
              private taskService: TaskService) {
  }

}
