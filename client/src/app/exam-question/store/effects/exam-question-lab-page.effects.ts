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
import {from} from 'rxjs/observable/from';
import {of} from 'rxjs/observable/of';
import {UtilService} from '../../../core/services/util.service';
import {ShowError, ShowWxQrcoode} from '../../../core/store/actions/core';
import {ExamQuestionLabService} from '../../services/exam-question-lab.service';
import {examQuestionLabActions, examQuestionLabPageActions} from '../../store';

@Injectable()
export class ExamQuestionLabPageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(examQuestionLabPageActions.INIT)
    .exhaustMap((action: any) => {
      return this.examQuestionLabService.list()
        .flatMap(labs => {
          const labId = action.labId || labs[0].id;
          const a1 = new examQuestionLabPageActions.InitSuccess(labId, labs);
          const a2 = new examQuestionLabPageActions.ListExamQuestion(labId);
          return from([a1, a2]);
        }).catch(error => of(new ShowError(error)));
    });

  @Effect()
  listExamQuestion$: Observable<Action> = this.actions$
    .ofType(examQuestionLabPageActions.LIST_EXAM_QUESTION)
    .exhaustMap((action: any) => {
      return this.examQuestionLabService.listExamQuestion(action.labId)
        .map(examQuestions => {
          return new examQuestionLabPageActions.ListExamQuestionSuccess(action.labId, examQuestions);
        }).catch(error => of(new ShowError(error)));
    });

  @Effect()
  invite$: Observable<Action> = this.actions$
    .ofType(examQuestionLabPageActions.INVITE)
    .exhaustMap((action: any) => {
      return this.examQuestionLabService.inviteTicket(action.labId)
        .map(res => new ShowWxQrcoode(res.ticket, 'BUTTON.INVITE'))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  deleteParticipant$: Observable<Action> = this.actions$
    .ofType(examQuestionLabActions.DELETE_PARTICIPANT)
    .exhaustMap((action: examQuestionLabActions.DeleteParticipant) => {
      return this.examQuestionLabService.deleteParticipant(action.labId, action.participantId)
        .switchMap(res => {
          this.utilService.showSuccess();
          return of();
        }).catch(error => of(new ShowError(error)));
    });

  @Effect()
  save$: Observable<Action> = this.actions$
    .ofType(examQuestionLabActions.SAVE)
    .exhaustMap((action: examQuestionLabActions.Save) => {
      return this.examQuestionLabService.save(action.lab)
        .map(res => {
          this.utilService.showSuccess();
          return action.lab.id ? new examQuestionLabActions.UpdateSuccess(res) : new examQuestionLabActions.CreateSuccess(res);
        }).catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions, private utilService: UtilService, private examQuestionLabService: ExamQuestionLabService) {
  }

}
