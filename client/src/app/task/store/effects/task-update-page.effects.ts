import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/withLatestFrom';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {taskUpdatePageActions} from '../';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {MyTaskGroup} from '../../../shared/models/task-group';
import {TaskService} from '../../services/task.service';

@Injectable()
export class TaskUpdatePageEffects {
  @Effect()
  save$: Observable<Action> = this.actions$
    .ofType(taskUpdatePageActions.SAVE)
    .exhaustMap((action: taskUpdatePageActions.Save) => {
      const {task} = action.payload;
      return this.taskService.save(task).switchMap(res => {
        this.utilService.showSuccess();
        this.router.navigate(['/tasks/progress'], {
          queryParams: {
            taskGroupId: (res.taskGroup || MyTaskGroup).id,
            taskId: res.id
          }
        });
        return of();
      }).catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions,
              private router: Router,
              private utilService: UtilService,
              private taskService: TaskService) {
  }

}
