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
import {of} from 'rxjs/observable/of';
import {enlistActions, enlistUpdatePageActions} from '../';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {Enlist} from '../../../shared/models/enlist';
import {EnlistService} from '../../services/enlist.service';

@Injectable()
export class EnlistUpdatePageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(enlistUpdatePageActions.INIT)
    .exhaustMap((action: enlistUpdatePageActions.Init) => {
      const {paymentMerchantId, enlistId} = action.payload;
      const enlist$ = enlistId ? this.enlistService.get(enlistId) : this.enlistService.getPaymentMerchant(paymentMerchantId).map(it => {
        const enlist = new Enlist();
        enlist.paymentMerchant = it;
        return enlist;
      });
      return enlist$
        .map(enlist => new enlistUpdatePageActions.InitSuccess(enlist))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  save$: Observable<Action> = this.actions$
    .ofType(enlistUpdatePageActions.SAVE)
    .exhaustMap((action: enlistUpdatePageActions.Save) => {
      return this.enlistService.save(action.enlist)
        .map(res => {
          this.utilService.showSuccess();
          this.router.navigate(['enlists', 'progress'], {
            queryParams: {
              enlistId: res.id
            }
          });
          return action.enlist.id ? new enlistActions.UpdateSuccess(res) : new enlistActions.CreateSuccess(res);
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
