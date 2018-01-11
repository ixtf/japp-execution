import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {baseApiUrl} from '../../../../environments/environment';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {shareTimelineTaskGroupPageActions} from '../../store';

@Injectable()
export class ShareTimelineTaskGroupPageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(shareTimelineTaskGroupPageActions.INIT)
    .exhaustMap((action: shareTimelineTaskGroupPageActions.Init) => {
      const {token} = action.cur;
      return this.http.get(`${baseApiUrl}/opens/shares/${token}/taskGroup`)
        .map((res: any) => {
          const {shareOperator, tasks, taskGroup} = res;
          return new shareTimelineTaskGroupPageActions.InitSuccess({
            shareOperator,
            taskGroup,
            taskObjects: tasks,
          });
        }).catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private utilService: UtilService,
              private http: HttpClient) {
  }

}
