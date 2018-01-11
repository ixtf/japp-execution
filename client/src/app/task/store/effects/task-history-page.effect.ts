import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {TaskService} from '../../services/task.service';

import {taskHistoryPageActions} from '../../store';

@Injectable()
export class TaskHistoryPageEffect {
  @Effect()
  historyList$: Observable<Action> = this.actions$
    .ofType(taskHistoryPageActions.INIT)
    .exhaustMap((action: taskHistoryPageActions.Init) => {
      return this.taskService.listHistory(action.first, action.pageSize)
        .map(res => new taskHistoryPageActions.InitSuccess(action.first, action.pageSize, res.count, res.tasks))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private dialog: MatDialog,
              private utilService: UtilService,
              private taskService: TaskService) {
  }

}
