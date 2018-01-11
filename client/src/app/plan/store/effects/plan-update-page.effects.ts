import {Injectable} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/withLatestFrom';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {planUpdatePageActions} from '../';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {Plan} from '../../../shared/models/plan';

@Injectable()
export class PlanUpdatePageEffects {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .filter(action => {
      const {event} = action.payload;
      return event.url.startsWith('/plans/edit');
    })
    .withLatestFrom(this.route.queryParams)
    .switchMap(a => {
      const [action, queryParams] = a;
      if (queryParams.id) {
        return this.apiService.getPlan(queryParams.id)
          .map(plan => {
            return new planUpdatePageActions.InitSuccess(plan);
          })
          .catch(error => of(new ShowError(error)));
      }
      return of(new planUpdatePageActions.InitSuccess(new Plan()));
    });

  @Effect()
  save$: Observable<Action> = this.actions$
    .ofType(planUpdatePageActions.SAVE)
    .exhaustMap((action: planUpdatePageActions.Save) => {
      return this.apiService.savePlan(action.plan)
        .switchMap(() => {
          this.utilService.showSuccess();
          this.router.navigate(['/plans']);
          return of();
        })
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions,
              private router: Router,
              private route: ActivatedRoute,
              private utilService: UtilService,
              private apiService: ApiService) {
  }
}
