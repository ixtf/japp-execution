import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {EditorModule} from '../editor/editor.module';
import {SharedModule} from '../shared/shared.module';
import {UploadModule} from '../upload/upload.module';
import {PlanItemInputComponent} from './components/plan-item-input/plan-item-input.component';
import {PlanItemUpdateDialogComponent} from './components/plan-item-update-dialog/plan-item-update-dialog.component';
import {PlanManagePageComponent} from './containers/plan-manage-page/plan-manage-page.component';
import {PlanUpdatePageComponent} from './containers/plan-update-page/plan-update-page.component';
import {PlanRoutingModule} from './plan-routing.module';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';

export const ENTRYCOMPONENTS = [
  PlanItemUpdateDialogComponent,
];
export const COMPONENTS = [
  PlanItemInputComponent,
  PlanUpdatePageComponent,
  PlanManagePageComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    UploadModule,
    EditorModule,
    SharedModule,
    PlanRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: []
})
export class PlanModule {
}
