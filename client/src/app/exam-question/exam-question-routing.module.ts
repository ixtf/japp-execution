import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ExamQuestionLabPageComponent} from './containers/exam-question-lab-page/exam-question-lab-page.component';
import {ExamQuestionReviewPageComponent} from './containers/exam-question-review-page/exam-question-review-page.component';
import {ExamQuestionUpdatePageComponent} from './containers/exam-question-update-page/exam-question-update-page.component';

const routes: Routes = [
  {
    path: 'labs',
    component: ExamQuestionLabPageComponent,
  },
  {
    path: 'edit',
    component: ExamQuestionUpdatePageComponent,
  },
  {
    path: 'review',
    component: ExamQuestionReviewPageComponent,
  },
  {
    path: 'reviewManage',
    component: ExamQuestionReviewPageComponent,
  },
  {path: '/examQuestions/**', redirectTo: '/examQuestions/labs', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ExamQuestionRoutingModule {
}
