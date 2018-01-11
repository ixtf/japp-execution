import {HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/withLatestFrom';
import {of} from 'rxjs/observable/of';
import {planManagePageActions, planManagePageState} from '../';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';

@Injectable()
export class PlanManagePageEffects {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .filter(action => {
      const {event} = action.payload;
      return event.url.startsWith('/plans');
    })
    .withLatestFrom(
      this.store.select(planManagePageState),
    )
    .exhaustMap(([action, state]) => {
      return this.route.queryParams.switchMap(({draft, audited, published, first, pageSize}) => {
        let httpParams = new HttpParams();
        draft = draft || false;
        audited = audited || false;
        published = published || false;
        first = first || 0;
        pageSize = pageSize || 100;
        if (audited) {
          httpParams = httpParams.set('audited', audited).set('published', 'true');
        } else if (published) {
          httpParams = httpParams.set('published', published);
        } else {
          draft = true;
        }
        httpParams = httpParams.set('first', first).set('pageSize', pageSize);
        return this.apiService.listPlan(httpParams)
          .map(res => {
            const data = Object.assign(res, {draft, audited, published});
            return new planManagePageActions.InitSuccess(data);
          })
          .catch(error => of(new ShowError(error)));
      });
    });

  constructor(private actions$: Actions,
              private store: Store<any>,
              private route: ActivatedRoute,
              private utilService: UtilService,
              private apiService: ApiService) {
  }
}
