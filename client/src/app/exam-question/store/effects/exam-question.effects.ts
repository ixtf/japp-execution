import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {ShowError} from '../../../core/store/actions/core';
import {ExamQuestionService} from '../../services/exam-question.service';
import {examQuestionActions} from '../../store';

@Injectable()
export class ExamQuestionEffects {
  @Effect()
  delete$: Observable<Action> = this.actions$
    .ofType(examQuestionActions.DELETE)
    .exhaustMap((action: any) => {
      return this.examQuestionService.delete(action.id)
        .map(res => new examQuestionActions.DeleteSuccess(action.id))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions, private examQuestionService: ExamQuestionService) {
  }

}
