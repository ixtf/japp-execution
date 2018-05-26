import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {EditorModule} from '../editor/editor.module';
import {PlanModule} from '../plan/plan.module';
import {SharedModule} from '../shared/shared.module';
import {TaskModule} from '../task/task.module';
import {UploadModule} from '../upload/upload.module';
import {AdminRoutingModule} from './admin-routing.module';
import {ChannelFormComponent} from './components/channel-form/channel-form.component';
import {ChannelListComponent} from './components/channel-list/channel-list.component';
import {PlanAuditDialogComponent} from './components/plan-audit-dialog/plan-audit-dialog.component';
import {ChannelManagePageComponent} from './containers/channel-manage-page/channel-manage-page.component';
import {PaymentMerchantManagePageComponent} from './containers/payment-merchant-manage-page/payment-merchant-manage-page.component';
import {PaymentMerchantUpdatePageComponent} from './containers/payment-merchant-update-page/payment-merchant-update-page.component';
import {PlanAuditPageComponent} from './containers/plan-audit-page/plan-audit-page.component';
import {RedEnvelopeOrganizationManagePageComponent} from './containers/red-envelope-organization-manage-page/red-envelope-organization-manage-page.component';
import {RedEnvelopeOrganizationUpdatePageComponent} from './containers/red-envelope-organization-update-page/red-envelope-organization-update-page.component';
import {TaskComplainHandlePageComponent} from './containers/task-complain-handle-page/task-complain-handle-page.component';
import {TaskManagePageComponent} from './containers/task-manage-page/task-manage-page.component';
import {AdminService} from './services/admin.service';
import {ChannelService} from './services/channel.service';
import {PaymentMerchantService} from './services/payment-merchant.service';
import {RedEnvelopeOrganizationService} from './services/red-envelope-organization.service';
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
  RedEnvelopeOrganizationManagePageComponent,
  RedEnvelopeOrganizationUpdatePageComponent,
  TaskManagePageComponent,
  TaskComplainHandlePageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    UploadModule,
    EditorModule,
    SharedModule,
    AdminRoutingModule,
    PlanModule,
    TaskModule,
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
    RedEnvelopeOrganizationService,
  ]
})
export class AdminModule {
}
