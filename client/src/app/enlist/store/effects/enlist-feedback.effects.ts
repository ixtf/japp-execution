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
import {EnlistFeedbackService} from '../../services/enlist-feedback.service';
import {enlistFeedbackActions} from '../../store';

@Injectable()
export class EnlistFeedbackEffects {
  @Effect()
  save$: Observable<Action> = this.actions$
    .ofType(enlistFeedbackActions.SAVE)
    .exhaustMap((action: enlistFeedbackActions.Save) => {
      const {enlistId, enlistFeedback} = action;
      return this.enlistFeedbackService.save(enlistId, enlistFeedback)
        .map(() => {
          if (!enlistFeedback.id) {
            alert('报名成功，请在当前页面继续完成付款！');
          }
          return new enlistFeedbackActions.List(enlistId);
        })
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  list$: Observable<Action> = this.actions$
    .ofType(enlistFeedbackActions.LIST)
    .mergeMap((action: enlistFeedbackActions.List) => {
      return this.enlistFeedbackService.list(action.enlistId)
        .map(res => new enlistFeedbackActions.ListSuccess(action.enlistId, res))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  delete$: Observable<Action> = this.actions$
    .ofType(enlistFeedbackActions.DELETE)
    .exhaustMap((action: enlistFeedbackActions.Delete) => {
      return this.enlistFeedbackService.delete(action.enlistFeedbackId)
        .map(() => new enlistFeedbackActions.List(action.enlistId))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions, private utilService: UtilService, private enlistFeedbackService: EnlistFeedbackService) {
  }

}
