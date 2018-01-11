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
import {shareTimelineTaskPageActions} from '../../store';

@Injectable()
export class ShareTimelineTaskPageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(shareTimelineTaskPageActions.INIT)
    .exhaustMap((action: shareTimelineTaskPageActions.Init) => {
      const {token} = action.cur;
      return this.http.get(`${baseApiUrl}/opens/shares/${token}/task`)
        .map((res: any) => {
          return new shareTimelineTaskPageActions.InitSuccess(res);
        }).catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private utilService: UtilService,
              private http: HttpClient) {
  }

}
