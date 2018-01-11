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
import {taskGroupActions} from '../';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {TaskGroupService} from '../../services/task-group.service';

@Injectable()
export class TaskGroupEffects {
  @Effect()
  get$: Observable<Action> = this.actions$
    .ofType(taskGroupActions.GET)
    .exhaustMap((action: taskGroupActions.Get) => {
      return this.taskGroupService.get(action.id)
        .map(res => new taskGroupActions.GetSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  top$: Observable<Action> = this.actions$
    .ofType(taskGroupActions.TOP)
    .exhaustMap((action: taskGroupActions.Top) => {
      return this.taskGroupService.top(action.taskGroup.id)
        .map(() => {
          const {taskGroup} = action;
          taskGroup.modifyDateTime = new Date();
          this.utilService.showSuccess();
          return new taskGroupActions.GetSuccess(taskGroup);
        })
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions,
              private utilService: UtilService,
              private taskGroupService: TaskGroupService) {
  }
}
