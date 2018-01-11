import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PaymentMerchantPayPageComponent} from './containers/payment-merchant-pay-page/payment-merchant-pay-page.component';

const routes: Routes = [
  {
    path: 'paymentMerchants/:paymentMerchantId',
    component: PaymentMerchantPayPageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WeixinPayRoutingModule {
}
