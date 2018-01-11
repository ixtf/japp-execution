import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {PaymentMerchant} from '../../../shared/models/payment-merchant';
import {PaymentMerchantManageDialogComponent} from '../../components/payment-merchant-manage-dialog/payment-merchant-manage-dialog.component';
import {paymentMerchantManagePageActions, paymentMerchantManagePagePaymentMerchants} from '../../store/index';

@Component({
  selector: 'jwjh-payment-merchant-manage-page',
  templateUrl: './payment-merchant-manage-page.component.html',
  styleUrls: ['./payment-merchant-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PaymentMerchantManagePageComponent extends DataSource<PaymentMerchant> {
  paymentMerchantDataSource = this;
  displayedColumns = ['name', 'btns'];
  paymentMerchants$: Observable<PaymentMerchant[]>;

  constructor(private store: Store<any>,
              private dialog: MatDialog) {
    super();
    this.paymentMerchants$ = this.store.select(paymentMerchantManagePagePaymentMerchants);
    this.store.dispatch(new paymentMerchantManagePageActions.Init());
  }

  showManagers(paymentMerchant: PaymentMerchant) {
    PaymentMerchantManageDialogComponent.open(this.dialog, {paymentMerchant});
  }

  invite(paymentMerchant: PaymentMerchant) {
    this.store.dispatch(new paymentMerchantManagePageActions.Invite(paymentMerchant.id));
  }

  connect(collectionViewer: CollectionViewer): Observable<PaymentMerchant[]> {
    return this.paymentMerchants$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

}
