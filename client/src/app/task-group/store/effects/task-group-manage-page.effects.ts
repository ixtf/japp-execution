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
import {taskGroupManagePageActions} from '../';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {TaskGroupService} from '../../services/task-group.service';

@Injectable()
export class TaskGroupManagePageEffects {

  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(taskGroupManagePageActions.INIT)
    .exhaustMap(() => {
      return this.taskGroupService.list()
        .map(res => new taskGroupManagePageActions.InitSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions,
              private utilService: UtilService,
              private taskGroupService: TaskGroupService) {
  }
}
