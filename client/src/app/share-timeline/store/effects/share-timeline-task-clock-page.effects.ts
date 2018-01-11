import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/withLatestFrom';
import {of} from 'rxjs/observable/of';
import {baseApiUrl} from '../../../../environments/environment';
import {ShowError} from '../../../core/store/actions/core';
import {Task} from '../../../shared/models/task';
import {shareTimelineTaskClockPageActions, taskClockPageState} from '../../store';

@Injectable()
export class ShareTimelineTaskClockPageEffects {
  @Effect() navigate$ = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .filter(action => {
      const {event} = action.payload;
      return event.url.startsWith('/shareTimeline/taskClock');
    })
    .withLatestFrom(this.store.map(taskClockPageState))
    .switchMap(a => {
      const [action, state] = a;
      const token = action.payload.routerState.root.queryParams['token'];
      return this.http.get<any>(`${baseApiUrl}/opens/shares/${token}/taskClock`)
        .map(res => {
          const {shareOperator, taskId} = res;
          const tasks = res.tasks.map(it => {
            const {taskEvaluates, taskFeedbacks, taskOperatorContextDatas} = it;
            const task = Task.assign(it.task, {taskEvaluates, taskFeedbacks, taskOperatorContextDatas});
            return task;
          });
          return new shareTimelineTaskClockPageActions.InitSuccess({shareOperator, taskId, tasks});
        })
        .catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private http: HttpClient) {
  }

}
