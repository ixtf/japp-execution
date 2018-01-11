import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as labPage from './reducers/exam-question-lab-page';
import * as reviewPage from './reducers/exam-question-review-page';

export * from './actions';

export const featureName = 'examQuestion';

export interface State {
  examQuestion: ExamQuestionState;
}

export interface ExamQuestionState {
  labPage: labPage.State;
  reviewPage: reviewPage.State;
}

export const reducers = {
  labPage: labPage.reducer,
  reviewPage: reviewPage.reducer,
};

export const examQuestionState = createFeatureSelector<ExamQuestionState>(featureName);
export const labPageState = createSelector(examQuestionState, state => state.labPage);
export const reviewPageState = createSelector(examQuestionState, state => state.reviewPage);

export const examQuestionLabPageLabs = createSelector(labPageState, labPage.getLabs);
export const examQuestionLabPageLab = createSelector(labPageState, labPage.getLab);
export const examQuestionLabPageExamQuestions = createSelector(labPageState, labPage.getExamQuestions);

export const examQuestionReviewPageTaskGroups = createSelector(reviewPageState, reviewPage.getTaskGroups);
export const examQuestionReviewPageTaskGroup = createSelector(reviewPageState, reviewPage.getTaskGroup);
export const examQuestionReviewPageSourceTasks = createSelector(reviewPageState, reviewPage.getSourceTasks);
export const examQuestionReviewPageDestTasks = createSelector(reviewPageState, reviewPage.getDestTasks);
export const examQuestionReviewPageJoinOperators = createSelector(reviewPageState, reviewPage.getJoinOperators);
