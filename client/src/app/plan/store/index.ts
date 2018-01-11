import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as planManagePage from './reducers/plan-manage-page';
import * as planUpdatePage from './reducers/plan-update-page';

export * from './actions';

export interface TaskGroupState {
  planUpdatePage: planUpdatePage.State;
  planManagePage: planManagePage.State;
}

export const reducers = {
  planUpdatePage: planUpdatePage.reducer,
  planManagePage: planManagePage.reducer,
};
export const featureName = 'plan';
export const planState = createFeatureSelector<TaskGroupState>(featureName);

export const planManagePageState = createSelector(planState, state => state.planManagePage);
export const planManagePageAudited = createSelector(planManagePageState, planManagePage.getAudited);
export const planManagePagePublished = createSelector(planManagePageState, planManagePage.getPublished);
export const planManagePageDraft = createSelector(planManagePageState, planManagePage.getDraft);
export const planManagePagePlans = createSelector(planManagePageState, planManagePage.getPlans);

export const planUpdatePageState = createSelector(planState, state => state.planUpdatePage);
export const planUpdatePagePlan = createSelector(planUpdatePageState, planUpdatePage.getPlan);
