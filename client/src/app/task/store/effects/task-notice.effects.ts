import {HttpClient} from '@angular/common/http';
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
import {baseApiUrl} from '../../../../environments/environment';
import {ShowError} from '../../../core/store/actions/core';
import {TaskNoticeService} from '../../services/task-notice.service';
import {taskNoticeActions} from '../../store';

@Injectable()
export class TaskNoticeEffects {
  @Effect()
  save$: Observable<Action> = this.actions$
    .ofType(taskNoticeActions.SAVE)
    .exhaustMap((action: taskNoticeActions.Save) => {
      return this.taskNoticeService.save(action.taskId, action.taskNotice)
        .map(taskNotice => action.taskNotice.id
          ? new taskNoticeActions.UpdateSuccess(action.taskId, taskNotice)
          : new taskNoticeActions.CreateSuccess(action.taskId, taskNotice))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  list$: Observable<Action> = this.actions$
    .ofType(taskNoticeActions.LIST)
    .exhaustMap((action: taskNoticeActions.List) => {
      return this.taskNoticeService.list(action.taskId)
        .map(taskNotices => new taskNoticeActions.ListSuccess(action.taskId, taskNotices))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  delete$: Observable<Action> = this.actions$
    .ofType(taskNoticeActions.DELETE)
    .exhaustMap((action: taskNoticeActions.Delete) => {
      return this.delete(action.taskId, action.taskNoticeId)
        .map(() => new taskNoticeActions.DeleteSuccess(action.taskId, action.taskNoticeId))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions,
              private http: HttpClient,
              private taskNoticeService: TaskNoticeService) {
  }

  delete(taskId: string, taskNoticeId: string): Observable<any> {
    return this.http.delete(`${baseApiUrl}/tasks/${taskId}/taskNotices/${taskNoticeId}`);
  }

}
