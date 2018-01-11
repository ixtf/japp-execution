import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {taskGroupActions, taskGroupUpdatePageActions} from '../';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {TaskGroup} from '../../../shared/models/task-group';
import {TaskGroupService} from '../../services/task-group.service';

@Injectable()
export class TaskGroupUpdatePageEffects {

  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(taskGroupUpdatePageActions.INIT)
    .exhaustMap((action: taskGroupUpdatePageActions.Init) => {
      const _$ = action.id ? this.taskGroupService.get(action.id) : of(new TaskGroup());
      return _$.map(res => new taskGroupUpdatePageActions.InitSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  save$: Observable<Action> = this.actions$
    .ofType(taskGroupUpdatePageActions.SAVE)
    .exhaustMap((action: taskGroupUpdatePageActions.Save) => {
      return this.taskGroupService.save(action.taskGroup)
        .map(res => {
          this.utilService.showSuccess();
          this.router.navigate(['/taskGroups']);
          return action.taskGroup.id
            ? new taskGroupActions.UpdateSuccess(res)
            : new taskGroupActions.CreateSuccess(res);
        })
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions,
              private router: Router,
              private utilService: UtilService,
              private taskGroupService: TaskGroupService) {
  }
}
