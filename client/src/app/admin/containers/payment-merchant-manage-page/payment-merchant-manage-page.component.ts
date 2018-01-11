import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {ChangeDetectionStrategy, Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {UtilService} from '../../../core/services/util.service';
import {PaymentMerchant} from '../../../shared/models/payment-merchant';
import {paymentMerchantManagePageActions, paymentMerchantManagePagePaymentMerchants} from '../../store/index';

@Component({
  selector: 'jwjh-admin-payment-merchant-manage-page',
  templateUrl: './payment-merchant-manage-page.component.html',
  styleUrls: ['./payment-merchant-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PaymentMerchantManagePageComponent extends DataSource<PaymentMerchant> {
  paymentMerchantDataSource = this;
  displayedColumns = ['name', 'sub_mch_id', 'btns'];
  paymentMerchants$: Observable<PaymentMerchant[]>;

  constructor(private store: Store<any>,
              private utilService: UtilService) {
    super();
    this.paymentMerchants$ = this.store.select(paymentMerchantManagePagePaymentMerchants);
    this.store.dispatch(new paymentMerchantManagePageActions.Init());
  }

  invite(paymentMerchant: PaymentMerchant) {
    this.store.dispatch(new paymentMerchantManagePageActions.Invite(paymentMerchant.id));
  }

  delete(paymentMerchant: PaymentMerchant) {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new paymentMerchantManagePageActions.Invite(paymentMerchant.id)));
  }

  connect(collectionViewer: CollectionViewer): Observable<PaymentMerchant[]> {
    return this.paymentMerchants$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

}
