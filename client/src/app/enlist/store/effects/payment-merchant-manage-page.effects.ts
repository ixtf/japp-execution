import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
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
import {EnlistService} from '../../services/enlist.service';
import {paymentMerchantManagePageActions} from '../../store';

@Injectable()
export class PaymentMerchantManagePageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(paymentMerchantManagePageActions.INIT)
    .exhaustMap(() => {
      return this.enlistService.listPaymentMerchant()
        .map(res => new paymentMerchantManagePageActions.InitSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  invite$: Observable<Action> = this.actions$
    .ofType(paymentMerchantManagePageActions.INVITE)
    .exhaustMap((action: paymentMerchantManagePageActions.Invite) => {
      return this.enlistService.paymentMerchantInvite(action.paymentMerchantId)
        .map(res => new ShowWxQrcoode(res.ticket, ''))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  deleteManager$: Observable<Action> = this.actions$
    .ofType(paymentMerchantManagePageActions.DELETE_MANAGER)
    .exhaustMap((action: paymentMerchantManagePageActions.DeleteManager) => {
      const {paymentMerchantId, managerId} = action.payload;
      return this.enlistService.deletePaymentMerchantManager(paymentMerchantId, managerId)
        .map(() => new paymentMerchantManagePageActions.DeleteManagerSuccess({paymentMerchantId, managerId}))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private http: HttpClient,
              private utilService: UtilService,
              private enlistService: EnlistService) {
  }
}
