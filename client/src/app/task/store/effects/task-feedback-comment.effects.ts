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
import {TaskFeedbackCommentService} from '../../services/task-feedback-comment.service';
import {taskFeedbackCommentActions} from '../../store';

@Injectable()
export class TaskFeedbackCommentEffects {
  @Effect()
  save$: Observable<Action> = this.actions$
    .ofType(taskFeedbackCommentActions.SAVE)
    .exhaustMap((action: taskFeedbackCommentActions.Save) => {
      return this.taskFeedbackCommentService.save(action.taskFeedbackId, action.taskFeedbackComment)
        .map(() => new taskFeedbackCommentActions.List(action.taskId, action.taskFeedbackId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  delete$: Observable<Action> = this.actions$
    .ofType(taskFeedbackCommentActions.DELETE)
    .exhaustMap((action: taskFeedbackCommentActions.Delete) => {
      return this.taskFeedbackCommentService.delete(action.taskFeedbackCommentId)
        .map(() => new taskFeedbackCommentActions.List(action.taskId, action.taskFeedbackId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  list$: Observable<Action> = this.actions$
    .ofType(taskFeedbackCommentActions.LIST)
    .exhaustMap((action: taskFeedbackCommentActions.List) => {
      return this.taskFeedbackCommentService.list(action.taskFeedbackId)
        .map(taskFeedbackComments => new taskFeedbackCommentActions.ListSuccess(action.taskId, action.taskFeedbackId, taskFeedbackComments))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions, private dialog: MatDialog, private taskFeedbackCommentService: TaskFeedbackCommentService) {
  }

}
