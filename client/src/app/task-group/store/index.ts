import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as taskGroupManagePage from './reducers/task-group-manage-page';
import * as taskGroupUpdatePage from './reducers/task-group-update-page';

export * from './actions';

export interface State {
  taskGroup: TaskGroupState;
}

export interface TaskGroupState {
  taskGroupUpdatePage: taskGroupUpdatePage.State;
  taskGroupManagePage: taskGroupManagePage.State;
}

export const reducers = {
  taskGroupUpdatePage: taskGroupUpdatePage.reducer,
  taskGroupManagePage: taskGroupManagePage.reducer,
};
export const featureName = 'taskGroup';
export const taskGroupState = createFeatureSelector<TaskGroupState>(featureName);

export const taskGroupManagePageState = createSelector(taskGroupState, state => state.taskGroupManagePage);
export const taskGroupManagePageTaskGroups = createSelector(taskGroupManagePageState, taskGroupManagePage.getTaskGroups);

export const taskGroupUpdatePageState = createSelector(taskGroupState, state => state.taskGroupUpdatePage);
export const taskGroupUpdatePageTaskGroup = createSelector(taskGroupUpdatePageState, taskGroupUpdatePage.getTaskGroup);
export const taskGroupUpdatePageTitle = createSelector(taskGroupUpdatePageState, taskGroupUpdatePage.getTitle);
