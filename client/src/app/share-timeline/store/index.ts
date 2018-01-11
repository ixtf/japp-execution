import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as taskClockPage from './reducers/share-timeline-task-clock-page';
import * as taskGroupPage from './reducers/share-timeline-task-group-page';
import * as taskPage from './reducers/share-timeline-task-page';

export * from './actions';

export interface ShareTimelineState {
  taskPage: taskPage.State;
  taskGroupPage: taskGroupPage.State;
  taskClockPage: taskClockPage.State;
}

export const featureName = 'shareTimeline';
export const reducers = {
  taskPage: taskPage.reducer,
  taskGroupPage: taskGroupPage.reducer,
  taskClockPage: taskClockPage.reducer,
};

export const shareTimelineState = createFeatureSelector<ShareTimelineState>(featureName);
export const taskPageState = createSelector(shareTimelineState, state => state.taskPage);
export const taskGroupPageState = createSelector(shareTimelineState, state => state.taskGroupPage);
export const taskClockPageState = createSelector(shareTimelineState, state => state.taskClockPage);

export const shareTimelineTaskPageShareOperator = createSelector(taskPageState, taskPage.getShareOperator);
export const shareTimelineTaskPageTask = createSelector(taskPageState, taskPage.getTask);
export const shareTimelineTaskPageTaskOperatorContextDatas = createSelector(taskPageState, taskPage.getTaskOperatorContextDatas);
export const shareTimelineTaskPageRightTabIndex = createSelector(taskPageState, taskPage.getRightTabIndex);

export const shareTimelineTaskGroupPageShareOperator = createSelector(taskGroupPageState, taskGroupPage.getShareOperator);
export const shareTimelineTaskGroupPageTaskGroup = createSelector(taskGroupPageState, taskGroupPage.getTaskGroup);
export const shareTimelineTaskGroupPageTasks = createSelector(taskGroupPageState, taskGroupPage.getTasks);
export const shareTimelineTaskGroupChartOptions = createSelector(taskGroupPageState, taskGroupPage.getChartOptions);
export const shareTimelineTaskGroupFinishedTaskEntities = createSelector(taskGroupPageState, taskGroupPage.getFinishedTaskEntities);
export const shareTimelineTaskGroupUnStartedTaskEntities = createSelector(taskGroupPageState, taskGroupPage.getUnStartedTaskEntities);

export const shareTimelineTaskClockPageNickName = createSelector(taskClockPageState, taskClockPage.getNickName);
export const shareTimelineTaskClockPageTaskGroup = createSelector(taskClockPageState, taskClockPage.getTaskGroup);
export const shareTimelineTaskClockPageTask = createSelector(taskClockPageState, taskClockPage.getTask);
export const shareTimelineTaskClockPageTasks = createSelector(taskClockPageState, taskClockPage.getTasks);
export const shareTimelineTaskClockPageDays = createSelector(taskClockPageState, taskClockPage.getDays);
export const shareTimelineTaskClockPagePercent = createSelector(taskClockPageState, taskClockPage.getPercent);
export const shareTimelineTaskClockPageChartOptions = createSelector(taskClockPageState, taskClockPage.getChartOptions);
