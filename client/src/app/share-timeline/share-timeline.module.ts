import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {ChartModule} from '../chart/chart.module';
import {EditorModule} from '../editor/editor.module';
import {SharedModule} from '../shared/shared.module';
import {UploadModule} from '../upload/upload.module';
import {ShareTimelineTaskDetailComponent} from './components/share-timeline-task-detail/share-timeline-task-detail.component';
import {ShareTimelineTaskClockPageComponent} from './containers/share-timeline-task-clock-page/share-timeline-task-clock-page.component';
import {ShareTimelineTaskGroupPageComponent} from './containers/share-timeline-task-group-page/share-timeline-task-group-page.component';
import {ShareTimelineTaskPageComponent} from './containers/share-timeline-task-page/share-timeline-task-page.component';
import {ShareTimelineRoutingModule} from './share-timeline-routing.module';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';

export const ENTRYCOMPONENTS = [];
export const COMPONENTS = [
  ShareTimelineTaskPageComponent,
  ShareTimelineTaskClockPageComponent,
  ShareTimelineTaskGroupPageComponent,
  ShareTimelineTaskDetailComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    UploadModule,
    EditorModule,
    ChartModule,
    SharedModule,
    ShareTimelineRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
})
export class ShareTimelineModule {
}
