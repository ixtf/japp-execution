import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {ChangeDetectionStrategy, Component} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {UtilService} from '../../../core/services/util.service';
import {RedEnvelopeOrganization} from '../../../shared/models/red-envelope-organization';
import {paymentMerchantManagePageActions, paymentMerchantManagePagePaymentMerchants} from '../../store/index';

@Component({
  selector: 'jwjh-admin-red-envelope-organization-manage-page',
  templateUrl: './red-envelope-organization-manage-page.component.html',
  styleUrls: ['./red-envelope-organization-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RedEnvelopeOrganizationManagePageComponent extends DataSource<RedEnvelopeOrganization> {
  paymentMerchantDataSource = this;
  displayedColumns = ['name', 'btns'];
  paymentMerchants$: Observable<RedEnvelopeOrganization[]>;

  constructor(private store: Store<any>,
              private utilService: UtilService) {
    super();
    this.paymentMerchants$ = this.store.select(paymentMerchantManagePagePaymentMerchants);
    this.store.dispatch(new paymentMerchantManagePageActions.Init());
  }

  invite(redEnvelopeOrganization: RedEnvelopeOrganization) {
    this.store.dispatch(new paymentMerchantManagePageActions.Invite(redEnvelopeOrganization.id));
  }

  delete(redEnvelopeOrganization: RedEnvelopeOrganization) {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new paymentMerchantManagePageActions.Invite(redEnvelopeOrganization.id)));
  }

  connect(collectionViewer: CollectionViewer): Observable<RedEnvelopeOrganization[]> {
    return this.paymentMerchants$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

}
