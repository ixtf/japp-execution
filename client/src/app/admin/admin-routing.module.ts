import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ChannelManagePageComponent} from './containers/channel-manage-page/channel-manage-page.component';
import {PaymentMerchantManagePageComponent} from './containers/payment-merchant-manage-page/payment-merchant-manage-page.component';
import {PaymentMerchantUpdatePageComponent} from './containers/payment-merchant-update-page/payment-merchant-update-page.component';
import {PlanAuditPageComponent} from './containers/plan-audit-page/plan-audit-page.component';
import {RedEnvelopeOrganizationManagePageComponent} from './containers/red-envelope-organization-manage-page/red-envelope-organization-manage-page.component';
import {RedEnvelopeOrganizationUpdatePageComponent} from './containers/red-envelope-organization-update-page/red-envelope-organization-update-page.component';
import {TaskComplainHandlePageComponent} from './containers/task-complain-handle-page/task-complain-handle-page.component';
import {TaskManagePageComponent} from './containers/task-manage-page/task-manage-page.component';

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
  {
    path: 'redEnvelopeOrganizations',
    component: RedEnvelopeOrganizationManagePageComponent,
  },
  {
    path: 'redEnvelopeOrganizationEdit',
    component: RedEnvelopeOrganizationUpdatePageComponent,
  },
  {
    path: 'tasks',
    component: TaskManagePageComponent,
  },
  {
    path: 'taskComplainHandle',
    component: TaskComplainHandlePageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {
}
