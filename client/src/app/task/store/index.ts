import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as batchPage from './reducers/task-batch-page';
import * as historyPage from './reducers/task-history-page';
import * as progressPage from './reducers/task-progress-page';

export * from './actions';

export interface TaskState {
  progressPage: progressPage.State;
  historyPage: historyPage.State;
  batchPage: batchPage.State;
}

export const featureName = 'task';
export const reducers = {
  progressPage: progressPage.reducer,
  historyPage: historyPage.reducer,
  batchPage: batchPage.reducer,
};
const taskState = createFeatureSelector<TaskState>(featureName);
const progressPageState = createSelector(taskState, state => state.progressPage);
const historyPageState = createSelector(taskState, state => state.historyPage);
const batchPageState = createSelector(taskState, state => state.batchPage);

export const taskProgressPageTaskId = createSelector(progressPageState, progressPage.getTaskId);
export const taskProgressPageStateRightTabIndex = createSelector(progressPageState, progressPage.getRightTabIndex);
export const taskProgressPageTaskGroups = createSelector(progressPageState, progressPage.getTaskGroups);
export const taskProgressPageTaskGroup = createSelector(progressPageState, progressPage.getTaskGroup);
export const taskProgressPageTasks = createSelector(progressPageState, progressPage.getTasks);
export const taskProgressPageTask = createSelector(progressPageState, progressPage.getTask);
export const taskProgressPageTaskFeedbacks = createSelector(progressPageState, progressPage.getTaskFeedbacks);
export const taskProgressPageTaskEvaluates = createSelector(progressPageState, progressPage.getTaskEvaluates);

export const taskHistoryPagePageSiz = createSelector(historyPageState, historyPage.getPageSiz);
export const taskHistoryPageCount = createSelector(historyPageState, historyPage.getCount);
export const taskHistoryPageTasks = createSelector(historyPageState, historyPage.getTasks);

export const taskBatchPageTaskGroups = createSelector(batchPageState, batchPage.getTaskGroups);
export const taskBatchPageTaskGroup = createSelector(batchPageState, batchPage.getTaskGroup);
export const taskBatchPageSourceTasks = createSelector(batchPageState, batchPage.getSourceTasks);
export const taskBatchPageDestTasks = createSelector(batchPageState, batchPage.getDestTasks);
