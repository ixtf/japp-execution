import {HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {of} from 'rxjs/observable/of';
import {catchError, combineLatest, exhaustMap, filter, map, switchMap} from 'rxjs/operators';
import {taskManagePageActions} from '..';
import {ApiService} from '../../../core/services/api.service';
import {ShowError} from '../../../core/store/actions/core';
import {Task} from '../../../shared/models/task';

@Injectable()
export class TaskManagePageEffects {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter((action: RouterNavigationAction) => {
        const {event} = action.payload;
        return event.url.startsWith('/admins/tasks');
      }),
      combineLatest(this.route.queryParams),
      switchMap((a: any[]) => {
        const [action, queryParams] = a;
        const {first, pageSize, q} = queryParams;
        let params = new HttpParams();
        if (first) {
          params = params.set('first', first);
        }
        if (pageSize) {
          params = params.set('pageSize', pageSize);
        }
        if (q) {
          params = params.set('q', q);
        }
        return this.apiService.listTask_Admin(params)
          .pipe(
            map((res: { tasks: Task[], count: number, first: number, pageSize: number }) => new taskManagePageActions.InitSuccess(res)),
            catchError(err => of(new ShowError(err)))
          );
      }),
    );

  constructor(private store: Store<any>,
              private actions$: Actions,
              private route: ActivatedRoute,
              private apiService: ApiService) {
  }
}
