import {Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {of} from 'rxjs/observable/of';
import {catchError, combineLatest, filter, map, switchMap} from 'rxjs/operators';
import {taskComplainHandlePageActions} from '..';
import {ApiService} from '../../../core/services/api.service';
import {ShowError} from '../../../core/store/actions/core';
import {Task} from '../../../shared/models/task';
import {TaskComplain} from '../../../shared/models/task-complain';

@Injectable()
export class TaskComplainHandlePageEffects {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .pipe(
      filter((action: RouterNavigationAction) => {
        const {event} = action.payload;
        return event.url.startsWith('/admins/taskComplainHandle');
      }),
      combineLatest(this.route.queryParams),
      switchMap((a: any[]) => {
        const [action, queryParams] = a;
        const {taskId, taskComplainId} = queryParams;
        return this.apiService.listTaskComplain_Admin(taskId)
          .pipe(
            map((res: { task: Task, taskComplains: TaskComplain[] }) => {
              const data = Object.assign(res, {initTaskComplainId: taskComplainId});
              return new taskComplainHandlePageActions.InitSuccess(data);
            }),
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
