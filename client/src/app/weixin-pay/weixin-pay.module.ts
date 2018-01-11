import {NgModule} from '@angular/core';
import {SharedModule} from '../shared/shared.module';
import {PaymentMerchantPayPageComponent} from './containers/payment-merchant-pay-page/payment-merchant-pay-page.component';
import {WeixinPayService} from './services/weixin-pay.service';
import {WeixinPayRoutingModule} from './weixin-pay-routing.module';

export const ENTRYCOMPONENTS = [];
export const COMPONENTS = [
  PaymentMerchantPayPageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    SharedModule,
    WeixinPayRoutingModule
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: [
    WeixinPayService,
  ]
})
export class WeixinPayModule {
}
