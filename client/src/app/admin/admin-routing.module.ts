import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ChannelManagePageComponent} from './containers/channel-manage-page/channel-manage-page.component';
import {PaymentMerchantManagePageComponent} from './containers/payment-merchant-manage-page/payment-merchant-manage-page.component';
import {PaymentMerchantUpdatePageComponent} from './containers/payment-merchant-update-page/payment-merchant-update-page.component';
import {PlanAuditPageComponent} from './containers/plan-audit-page/plan-audit-page.component';

const routes: Routes = [
  {
    path: 'channels',
    component: ChannelManagePageComponent,
  },
  {
    path: 'plans',
    component: PlanAuditPageComponent,
  },
  {
    path: 'paymentMerchants',
    component: PaymentMerchantManagePageComponent,
  },
  {
    path: 'paymentMerchants/edit',
    component: PaymentMerchantUpdatePageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {
}
