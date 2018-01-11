import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Actions, Effect} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {ShowError} from '../../../core/store/actions/core';
import {TaskEvaluateService} from '../../services/task-evaluate.service';
import {taskEvaluateActions} from '../../store';

@Injectable()
export class TaskEvaluateEffects {
  @Effect()
  save$: Observable<Action> = this.actions$
    .ofType(taskEvaluateActions.SAVE)
    .exhaustMap((action: taskEvaluateActions.Save) => {
      return this.taskEvaluateService.save(action.taskId, action.taskEvaluate)
        .map(() => new taskEvaluateActions.List(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  list$: Observable<Action> = this.actions$
    .ofType(taskEvaluateActions.LIST)
    .exhaustMap((action: taskEvaluateActions.List) => {
      return this.taskEvaluateService.list(action.taskId)
        .map(taskEvaluates => new taskEvaluateActions.ListSuccess(action.taskId, taskEvaluates))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  delete$: Observable<Action> = this.actions$
    .ofType(taskEvaluateActions.DELETE)
    .exhaustMap((action: taskEvaluateActions.Delete) => {
      return this.taskEvaluateService.delete(action.taskEvaluateId)
        .map(() => new taskEvaluateActions.List(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions, private dialog: MatDialog, private taskEvaluateService: TaskEvaluateService) {
  }

}
