import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {ChartModule} from '../chart/chart.module';
import {EditorModule} from '../editor/editor.module';
import {SharedModule} from '../shared/shared.module';
import {TaskGroupService} from '../task-group/services/task-group.service';
import {UploadModule} from '../upload/upload.module';
import {ExamQuestionInputComponent} from './components/exam-question-input/exam-question-input.component';
import {ExamQuestionPickDialogComponent} from './components/exam-question-pick-dialog/exam-question-pick-dialog.component';
import {NicknamesUpdateDialogComponent} from './components/nicknames-update-dialog/nicknames-update-dialog.component';
import {TaskEvaluateListComponent} from './components/task-evaluate-list/task-evaluate-list.component';
import {TaskEvaluateUpdateDialogComponent} from './components/task-evaluate-update-dialog/task-evaluate-update-dialog.component';
import {TaskEvaluateComponent} from './components/task-evaluate/task-evaluate.component';
import {TaskFeedbackCommentListComponent} from './components/task-feedback-comment-list/task-feedback-comment-list.component';
import {TaskFeedbackCommentUpdateDialogComponent} from './components/task-feedback-comment-update-dialog/task-feedback-comment-update-dialog.component';
import {TaskFeedbackCommentComponent} from './components/task-feedback-comment/task-feedback-comment.component';
import {TaskFeedbackListComponent} from './components/task-feedback-list/task-feedback-list.component';
import {TaskFeedbackUpdateDialogComponent} from './components/task-feedback-update-dialog/task-feedback-update-dialog.component';
import {TaskFeedbackComponent} from './components/task-feedback/task-feedback.component';
import {TaskNoticeUpdateDialogComponent} from './components/task-notice-update-dialog/task-notice-update-dialog.component';
import {TaskOperatorDialogComponent} from './components/task-operator-dialog/task-operator-dialog.component';
import {TaskOperatorImportDialogComponent} from './components/task-operator-import-dialog/task-operator-import-dialog.component';
import {TaskUnfeedbackerDialogComponent} from './components/task-unfeedbacker-dialog/task-unfeedbacker-dialog.component';
import {TaskUpdateFormComponent} from './components/task-update-form/task-update-form.component';
import {TaskBatchPageComponent} from './containers/task-batch-page/task-batch-page.component';
import {TaskHistoryPageComponent} from './containers/task-history-page/task-history-page.component';
import {TaskProgressLeftComponent} from './containers/task-progress-left/task-progress-left.component';
import {TaskProgressPageComponent} from './containers/task-progress-page/task-progress-page.component';
import {TaskProgressRightComponent} from './containers/task-progress-right/task-progress-right.component';
import {TaskUpdatePageComponent} from './containers/task-update-page/task-update-page.component';
import {TaskBatchService} from './services/task-batch.service';
import {TaskEvaluateService} from './services/task-evaluate.service';
import {TaskFeedbackCommentService} from './services/task-feedback-comment.service';
import {TaskFeedbackService} from './services/task-feedback.service';
import {TaskNoticeService} from './services/task-notice.service';
import {TaskService} from './services/task.service';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects';
import {TaskRoutingModule} from './task-routing.module';

export const ENTRYCOMPONENTS = [
  TaskFeedbackUpdateDialogComponent,
  TaskFeedbackCommentUpdateDialogComponent,
  TaskEvaluateUpdateDialogComponent,
  TaskOperatorDialogComponent,
  TaskOperatorImportDialogComponent,
  TaskUnfeedbackerDialogComponent,
  TaskNoticeUpdateDialogComponent,
  NicknamesUpdateDialogComponent,
  ExamQuestionPickDialogComponent,
];
export const COMPONENTS = [
  TaskProgressPageComponent,
  TaskProgressLeftComponent,
  TaskProgressRightComponent,
  TaskUpdateFormComponent,
  TaskBatchPageComponent,
  TaskUpdatePageComponent,
  TaskFeedbackListComponent,
  TaskFeedbackComponent,
  TaskEvaluateListComponent,
  TaskEvaluateComponent,
  TaskFeedbackCommentListComponent,
  TaskFeedbackCommentComponent,
  TaskHistoryPageComponent,
  ExamQuestionInputComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    UploadModule,
    EditorModule,
    ChartModule,
    SharedModule,
    TaskRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: [
    TaskGroupService,
    TaskBatchService,
    TaskService,
    TaskNoticeService,
    TaskFeedbackService,
    TaskFeedbackCommentService,
    TaskEvaluateService,
  ]
})
export class TaskModule {
}
