import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Actions, Effect} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {UtilService} from '../../../core/services/util.service';
import {coreAuthOperator} from '../../../core/store';
import {ShowError} from '../../../core/store/actions/core';
import {TaskService} from '../../services/task.service';
import {taskActions} from '../../store';

@Injectable()
export class TaskEffects {
  @Effect()
  get$: Observable<Action> = this.actions$
    .ofType(taskActions.GET)
    .exhaustMap((action: taskActions.Get) => {
      return this.taskService.get(action.id)
        .map(res => new taskActions.GetSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  top$: Observable<Action> = this.actions$
    .ofType(taskActions.TOP)
    .exhaustMap((action: taskActions.Top) => {
      return this.taskService.top(action.task.id)
        .map(() => {
          const task = action.task;
          task.modifyDateTime = new Date();
          this.utilService.showSuccess();
          return new taskActions.GetSuccess(task);
        })
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  copy$: Observable<Action> = this.actions$
    .ofType(taskActions.COPY)
    .exhaustMap((action: taskActions.Copy) => {
      return this.taskService.copy(action.taskId)
        .map(res => {
          this.utilService.showSuccess();
          return new taskActions.GetSuccess(res);
        })
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  read$: Observable<Action> = this.actions$
    .ofType(taskActions.READ)
    .exhaustMap((action: taskActions.Read) => {
      return this.taskService.read(action.taskId)
        .delay(3000)
        .map(() => new taskActions.GetOperatorContextData(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  done$: Observable<Action> = this.actions$
    .ofType(taskActions.DONE)
    .exhaustMap((action: taskActions.Done) => {
      return this.taskService.done(action.taskId)
        .map(() => new taskActions.DoneSuccess(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  restart$: Observable<Action> = this.actions$
    .ofType(taskActions.RESTART)
    .exhaustMap((action: taskActions.Restart) => {
      return this.taskService.restart(action.taskId)
        .map(() => new taskActions.RestartSuccess(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  delete$: Observable<Action> = this.actions$
    .ofType(taskActions.DELETE)
    .exhaustMap((action: taskActions.Delete) => {
      return this.taskService.delete(action.taskId)
        .map(() => new taskActions.DeleteSuccess(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  quit$: Observable<Action> = this.actions$
    .ofType<taskActions.Quit>(taskActions.QUIT)
    .exhaustMap(action => {
      return this.taskService.quit(action.taskId)
        .map(() => new taskActions.QuitSuccess(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  deleteFollower$: Observable<Action> = this.actions$
    .ofType(taskActions.DELETE_FOLLOWER)
    .exhaustMap((action: taskActions.DeleteFollower) => {
      return this.taskService.deleteFollower(action.taskId, action.followerId)
        .map(() => new taskActions.Get(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  importFollowers$: Observable<Action> = this.actions$
    .ofType(taskActions.IMPORT_FOLLOWERS)
    .filter((action: taskActions.ImportFollowers) => action.followers && action.followers.length > 0)
    .exhaustMap((action: taskActions.ImportFollowers) => {
      return this.taskService.importFollowers(action.taskId, action.followers)
        .map(() => new taskActions.Get(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  deleteParticipant$: Observable<Action> = this.actions$
    .ofType(taskActions.DELETE_PARTICIPANT)
    .exhaustMap((action: taskActions.DeleteParticipant) => {
      return this.taskService.deleteParticipant(action.taskId, action.participantId)
        .map(() => new taskActions.Get(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  importParticipants$: Observable<Action> = this.actions$
    .ofType(taskActions.IMPORT_PARTICIPANTS)
    .filter((action: taskActions.ImportParticipants) => action.participants && action.participants.length > 0)
    .exhaustMap((action: taskActions.ImportParticipants) => {
      return this.taskService.importParticipants(action.taskId, action.participants)
        .map(() => new taskActions.Get(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  getContextData$: Observable<Action> = this.actions$
    .ofType(taskActions.GET_CONTEXT_DATA)
    .mergeMap((action: taskActions.GetContextData) => {
      return this.taskService.getContextData(action.taskId)
        .map(res => new taskActions.GetContextDataSuccess(action.taskId, res))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  getOperatorContextData$: Observable<Action> = this.actions$
    .ofType(taskActions.GET_OPERATOR_CONTEXT_DATA)
    .mergeMap((action: taskActions.GetOperatorContextData) => {
      return this.store.select(coreAuthOperator).take(1)
        .mergeMap(
          (operator => this.taskService.getOperatorContextData(action.taskId, operator.id)),
          (operator, res) => new taskActions.GetOperatorContextDataSuccess(action.taskId, res)
        );
    });

  constructor(private actions$: Actions,
              private store: Store<any>,
              private dialog: MatDialog,
              private utilService: UtilService,
              private taskService: TaskService) {
  }

}
