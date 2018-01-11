import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {ChartModule} from '../chart/chart.module';
import {EditorModule} from '../editor/editor.module';
import {SharedModule} from '../shared/shared.module';
import {UploadModule} from '../upload/upload.module';
import {EnlistFeedbackListComponent} from './components/enlist-feedback-list/enlist-feedback-list.component';
import {EnlistFeedbackUpdateDialogComponent} from './components/enlist-feedback-update-dialog/enlist-feedback-update-dialog.component';
import {EnlistFeedbackComponent} from './components/enlist-feedback/enlist-feedback.component';
import {EnlistGenerateTaskDialogComponent} from './components/enlist-generate-task-dialog/enlist-generate-task-dialog.component';
import {EnlistOperatorDialogComponent} from './components/enlist-operator-dialog/enlist-operator-dialog.component';
import {EnlistUpdateFormComponent} from './components/enlist-update-form/enlist-update-form.component';
import {PaymentMerchantManageDialogComponent} from './components/payment-merchant-manage-dialog/payment-merchant-manage-dialog.component';
import {EnlistHistoryPageComponent} from './containers/enlist-history-page/enlist-history-page.component';
import {EnlistProgressLeftComponent} from './containers/enlist-progress-left/enlist-progress-left.component';
import {EnlistProgressPageComponent} from './containers/enlist-progress-page/enlist-progress-page.component';
import {EnlistProgressRightComponent} from './containers/enlist-progress-right/enlist-progress-right.component';
import {EnlistUpdatePageComponent} from './containers/enlist-update-page/enlist-update-page.component';
import {PaymentMerchantManagePageComponent} from './containers/payment-merchant-manage-page/payment-merchant-manage-page.component';
import {EnlistRoutingModule} from './enlist-routing.module';
import {EnlistFeedbackService} from './services/enlist-feedback.service';
import {EnlistService} from './services/enlist.service';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects/index';

export const ENTRYCOMPONENTS = [
  EnlistFeedbackUpdateDialogComponent,
  EnlistOperatorDialogComponent,
  EnlistGenerateTaskDialogComponent,
  PaymentMerchantManageDialogComponent,
];
export const COMPONENTS = [
  PaymentMerchantManagePageComponent,
  EnlistProgressPageComponent,
  EnlistProgressLeftComponent,
  EnlistProgressRightComponent,
  EnlistUpdateFormComponent,
  EnlistUpdatePageComponent,
  EnlistFeedbackListComponent,
  EnlistFeedbackComponent,
  EnlistHistoryPageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    UploadModule,
    EditorModule,
    ChartModule,
    SharedModule,
    EnlistRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: [
    EnlistService,
    EnlistFeedbackService,
  ]
})
export class EnlistModule {
}
