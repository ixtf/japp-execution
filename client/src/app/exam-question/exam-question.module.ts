import {NgModule} from '@angular/core';
import {EffectsModule} from '@ngrx/effects';
import {StoreModule} from '@ngrx/store';
import {EditorModule} from '../editor/editor.module';
import {SharedModule} from '../shared/shared.module';
import {TaskBatchService} from '../task/services/task-batch.service';
import {UploadModule} from '../upload/upload.module';
import {ExamQuestionFormComponent} from './components/exam-question-form/exam-question-form.component';
import {ExamQuestionLabOperatorDialogComponent} from './components/exam-question-lab-operator-dialog/exam-question-lab-operator-dialog.component';
import {ExamQuestionListComponent} from './components/exam-question-list/exam-question-list.component';
import {ExamQuestionReviewOperatorDialogComponent} from './components/exam-question-review-operator-dialog/exam-question-review-operator-dialog.component';
import {ExamQuestionLabPageComponent} from './containers/exam-question-lab-page/exam-question-lab-page.component';
import {ExamQuestionReviewPageComponent} from './containers/exam-question-review-page/exam-question-review-page.component';
import {ExamQuestionUpdatePageComponent} from './containers/exam-question-update-page/exam-question-update-page.component';
import {ExamQuestionRoutingModule} from './exam-question-routing.module';
import {ExamQuestionLabService} from './services/exam-question-lab.service';
import {ExamQuestionService} from './services/exam-question.service';
import {featureName, reducers} from './store';
import {featureEffects} from './store/effects/index';

export const ENTRYCOMPONENTS = [
  ExamQuestionLabOperatorDialogComponent,
  ExamQuestionReviewOperatorDialogComponent,
];
export const COMPONENTS = [
  ExamQuestionReviewPageComponent,
  ExamQuestionLabPageComponent,
  ExamQuestionListComponent,
  ExamQuestionUpdatePageComponent,
  ExamQuestionFormComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    UploadModule,
    EditorModule,
    SharedModule,
    ExamQuestionRoutingModule,
    StoreModule.forFeature(featureName, reducers),
    EffectsModule.forFeature(featureEffects),
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: [
    ExamQuestionLabService,
    ExamQuestionService,
    TaskBatchService,
  ]
})
export class ExamQuestionModule {
}
