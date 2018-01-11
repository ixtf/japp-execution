import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {EditorModule} from '../editor/editor.module';
import {SharedModule} from '../shared/shared.module';
import {UploadModule} from '../upload/upload.module';
import {TaskGroupListComponent} from './components/task-group-list/task-group-list.component';
import {TaskGroupUpdateComponent} from './components/task-group-update/task-group-update.component';
import {PickTaskGroupDialogComponent} from './containers/pick-task-group-dialog/pick-task-group-dialog.component';
import {TaskGroupManagePageComponent} from './containers/task-group-manage-page/task-group-manage-page.component';
import {TaskGroupUpdatePageComponent} from './containers/task-group-update-page/task-group-update-page.component';
import {TaskGroupService} from './services/task-group.service';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';
import {TaskGroupRoutingModule} from './task-group-routing.module';

export const ENTRYCOMPONENTS = [
  PickTaskGroupDialogComponent,
];
export const COMPONENTS = [
  TaskGroupManagePageComponent,
  TaskGroupUpdatePageComponent,
  TaskGroupListComponent,
  TaskGroupUpdateComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    UploadModule,
    EditorModule,
    SharedModule,
    TaskGroupRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: [
    TaskGroupService,
  ]
})
export class TaskGroupModule {
}
