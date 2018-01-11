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
import {EnlistService} from '../../services/enlist.service';

import {enlistHistoryPageActions} from '../../store';

@Injectable()
export class EnlistHistoryPageEffect {
  @Effect()
  historyList$: Observable<Action> = this.actions$
    .ofType(enlistHistoryPageActions.LIST)
    .exhaustMap((action: any) => {
      return this.enlistService.listHistory(action.first, action.pageSize)
        .map(res => new enlistHistoryPageActions.ListSuccess(action.first, action.pageSize, res.count, res.enlists))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private dialog: MatDialog,
              private utilService: UtilService,
              private enlistService: EnlistService) {
  }

}
