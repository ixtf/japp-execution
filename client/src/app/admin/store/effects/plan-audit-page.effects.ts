import {HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/withLatestFrom';
import {of} from 'rxjs/observable/of';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {planManagePageState} from '../../../plan/store';
import {planAuditPageActions} from '../../store';

@Injectable()
export class PlanAuditPageEffects {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .filter(action => {
      const {event} = action.payload;
      return event.url.startsWith('/admins/plans');
    })
    .withLatestFrom(
      this.store.select(planManagePageState),
    )
    .exhaustMap(([action, state]) => {
      return this.route.queryParams.switchMap(({first, pageSize}) => {
        let httpParams = new HttpParams();
        first = first || 0;
        pageSize = pageSize || 100;
        httpParams = httpParams.set('first', first).set('pageSize', pageSize).set('audited', 'false').set('published', 'true');
        return this.apiService.listPlan_Admin(httpParams)
          .map(res => {
            return new planAuditPageActions.InitSuccess(res);
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
