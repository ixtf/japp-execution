import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {EditorModule} from '../editor/editor.module';
import {PlanModule} from '../plan/plan.module';
import {SharedModule} from '../shared/shared.module';
import {UploadModule} from '../upload/upload.module';
import {AdminRoutingModule} from './admin-routing.module';
import {ChannelFormComponent} from './components/channel-form/channel-form.component';
import {ChannelListComponent} from './components/channel-list/channel-list.component';
import {PlanAuditDialogComponent} from './components/plan-audit-dialog/plan-audit-dialog.component';
import {ChannelManagePageComponent} from './containers/channel-manage-page/channel-manage-page.component';
import {PaymentMerchantManagePageComponent} from './containers/payment-merchant-manage-page/payment-merchant-manage-page.component';
import {PaymentMerchantUpdatePageComponent} from './containers/payment-merchant-update-page/payment-merchant-update-page.component';
import {PlanAuditPageComponent} from './containers/plan-audit-page/plan-audit-page.component';
import {AdminService} from './services/admin.service';
import {ChannelService} from './services/channel.service';
import {PaymentMerchantService} from './services/payment-merchant.service';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';

export const ENTRYCOMPONENTS = [
  PlanAuditDialogComponent,
];
export const COMPONENTS = [
  ChannelManagePageComponent,
  PlanAuditPageComponent,
  ChannelListComponent,
  ChannelFormComponent,
  PaymentMerchantManagePageComponent,
  PaymentMerchantUpdatePageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    UploadModule,
    EditorModule,
    SharedModule,
    AdminRoutingModule,
    PlanModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: [
    AdminService,
    ChannelService,
    PaymentMerchantService,
  ]
})
export class AdminModule {
}
