import {ChangeDetectionStrategy, Component, Inject, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Actions} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {UtilService} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {PaymentMerchant} from '../../../shared/models/payment-merchant';
import {paymentMerchantManagePageActions} from '../../store';

@Component({
  selector: 'jwjh-payment-merchant-manage-dialog',
  templateUrl: './payment-merchant-manage-dialog.component.html',
  styleUrls: ['./payment-merchant-manage-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PaymentMerchantManageDialogComponent implements OnDestroy {
  paymentMerchant: PaymentMerchant;
  private subscriptions = [];

  constructor(private actions$: Actions,
              private store: Store<any>,
              private utilService: UtilService,
              public dialogRef: MatDialogRef<PaymentMerchantManageDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { paymentMerchant: PaymentMerchant }) {
    this.paymentMerchant = data.paymentMerchant;
  }

  static open(dialog: MatDialog, data: { paymentMerchant: PaymentMerchant }): MatDialogRef<PaymentMerchantManageDialogComponent> {
    return dialog.open(PaymentMerchantManageDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  delete(operator: Operator) {
    const action = new paymentMerchantManagePageActions.DeleteManager({
      paymentMerchantId: this.paymentMerchant.id,
      managerId: operator.id
    });
    this.utilService.showConfirm().subscribe(() => this.store.dispatch(action));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
