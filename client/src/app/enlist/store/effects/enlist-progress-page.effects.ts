import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {from} from 'rxjs/observable/from';
import {of} from 'rxjs/observable/of';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {EnlistService} from '../../services/enlist.service';
import {enlistFeedbackActions, enlistProgressPageActions} from '../../store';

@Injectable()
export class EnlistProgressPageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(enlistProgressPageActions.INIT)
    .exhaustMap((action: enlistProgressPageActions.Init) => {
      return this.enlistService.list().switchMap(res => {
        const {cur} = action;
        let actions: Action[] = [new enlistProgressPageActions.InitSuccess(res, cur.enlistId)];
        actions = actions.concat(res.map(it => new enlistFeedbackActions.List(it.id)));
        return from(actions);
      }).catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private dialog: MatDialog,
              private utilService: UtilService,
              private enlistService: EnlistService) {
  }

}
