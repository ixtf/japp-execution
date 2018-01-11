import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {ShowError, ShowWxQrcoode} from '../../../core/store/actions/core';
import {EnlistService} from '../../services/enlist.service';
import {enlistActions} from '../../store';

@Injectable()
export class EnlistEffects {
  @Effect()
  get$: Observable<Action> = this.actions$
    .ofType(enlistActions.GET)
    .exhaustMap((action: enlistActions.Get) => {
      return this.enlistService.get(action.enlistId)
        .map(res => new enlistActions.GetSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  invite$: Observable<Action> = this.actions$
    .ofType(enlistActions.INVITE)
    .exhaustMap((action: enlistActions.Invite) => {
      return this.enlistService.invite(action.id)
        .map(res => new ShowWxQrcoode(res.ticket, ''))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  done$: Observable<Action> = this.actions$
    .ofType(enlistActions.DONE)
    .exhaustMap((action: enlistActions.Done) => {
      return this.enlistService.done(action.enlistId)
        .map(() => new enlistActions.DoneSuccess(action.enlistId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  restart$: Observable<Action> = this.actions$
    .ofType(enlistActions.RESTART)
    .exhaustMap((action: enlistActions.Restart) => {
      return this.enlistService.restart(action.enlistId)
        .map(() => new enlistActions.RestartSuccess(action.enlistId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  delete$: Observable<Action> = this.actions$
    .ofType(enlistActions.DELETE)
    .exhaustMap((action: enlistActions.Delete) => {
      return this.enlistService.delete(action.enlistId)
        .map(it => new enlistActions.DeleteSuccess(action.enlistId))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  generateTask$: Observable<Action> = this.actions$
    .ofType(enlistActions.GENERATE_TASK)
    .exhaustMap((action: enlistActions.GenerateTask) => {
      const {enlistId, taskGroup, taskGroupName} = action.payload;
      return this.enlistService.generateTask(enlistId, taskGroup, taskGroupName)
        .switchMap(task => {
          this.router.navigate(['/tasks/edit'], {queryParams: {taskId: task.id}});
          return of();
        })
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions,
              private router: Router,
              private enlistService: EnlistService) {
  }

}
