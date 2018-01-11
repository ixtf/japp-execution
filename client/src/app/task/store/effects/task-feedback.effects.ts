import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {TaskFeedbackService} from '../../services/task-feedback.service';
import {taskFeedbackActions} from '../../store';

@Injectable()
export class TaskFeedbackEffects {
  @Effect()
  save$: Observable<Action> = this.actions$
    .ofType(taskFeedbackActions.SAVE)
    .exhaustMap((action: taskFeedbackActions.Save) => {
      return this.taskFeedbackService.save(action.taskId, action.taskFeedback)
        .map(() => new taskFeedbackActions.List(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  list$: Observable<Action> = this.actions$
    .ofType(taskFeedbackActions.LIST)
    .exhaustMap((action: taskFeedbackActions.List) => {
      return this.taskFeedbackService.list(action.taskId)
        .map(taskFeedbacks => new taskFeedbackActions.ListSuccess(action.taskId, taskFeedbacks))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  delete$: Observable<Action> = this.actions$
    .ofType(taskFeedbackActions.DELETE)
    .exhaustMap((action: taskFeedbackActions.Delete) => {
      return this.taskFeedbackService.delete(action.taskFeedbackId)
        .map(() => new taskFeedbackActions.List(action.taskId))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions, private utilService: UtilService, private taskFeedbackService: TaskFeedbackService) {
  }

}
