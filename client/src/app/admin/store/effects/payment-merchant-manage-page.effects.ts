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
import {ShowError, ShowWxQrcoode} from '../../../core/store/actions/core';
import {PaymentMerchantService} from '../../services/payment-merchant.service';
import {paymentMerchantManagePageActions} from '../../store';

@Injectable()
export class PaymentMerchantManagePageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(paymentMerchantManagePageActions.INIT)
    .exhaustMap(() => {
      return this.paymentMerchantService.list()
        .map(res => new paymentMerchantManagePageActions.InitSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  invite$: Observable<Action> = this.actions$
    .ofType(paymentMerchantManagePageActions.INVITE)
    .exhaustMap((action: paymentMerchantManagePageActions.Invite) => {
      return this.paymentMerchantService.invite(action.paymentMerchantId)
        .map(res => new ShowWxQrcoode(res.ticket, ''))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private dialog: MatDialog,
              private utilService: UtilService,
              private paymentMerchantService: PaymentMerchantService) {
  }
}
