import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {EnlistHistoryPageComponent} from './containers/enlist-history-page/enlist-history-page.component';
import {EnlistProgressPageComponent} from './containers/enlist-progress-page/enlist-progress-page.component';
import {EnlistUpdatePageComponent} from './containers/enlist-update-page/enlist-update-page.component';
import {PaymentMerchantManagePageComponent} from './containers/payment-merchant-manage-page/payment-merchant-manage-page.component';

const routes: Routes = [
  {
    path: 'paymentMerchants',
    component: PaymentMerchantManagePageComponent,
  },
  {
    path: 'progress',
    component: EnlistProgressPageComponent,
  },
  {
    path: 'history',
    component: EnlistHistoryPageComponent,
  },
  {
    path: 'edit',
    component: EnlistUpdatePageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EnlistRoutingModule {
}
