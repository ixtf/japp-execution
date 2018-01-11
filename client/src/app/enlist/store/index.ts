import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as historyPage from './reducers/enlist-history-page';
import * as progressPage from './reducers/enlist-progress-page';
import * as updatePage from './reducers/enlist-update-page';
import * as paymentMerchantManagePage from './reducers/payment-merchant-manage-page';

export * from './actions';

export interface State {
  enlist: EnlistState;
}

export interface EnlistState {
  paymentMerchantManagePage: paymentMerchantManagePage.State;
  progressPage: progressPage.State;
  historyPage: historyPage.State;
  updatePage: updatePage.State;
}

export const reducers = {
  paymentMerchantManagePage: paymentMerchantManagePage.reducer,
  progressPage: progressPage.reducer,
  historyPage: historyPage.reducer,
  updatePage: updatePage.reducer,
};

export const featureName = 'enlist';
export const featureState = createFeatureSelector<EnlistState>(featureName);
export const paymentMerchantManagePageState = createSelector(featureState, state => state.paymentMerchantManagePage);
export const progressPageState = createSelector(featureState, state => state.progressPage);
export const historyPageState = createSelector(featureState, state => state.historyPage);
export const updatePageState = createSelector(featureState, state => state.updatePage);

export const paymentMerchantManagePagePaymentMerchants = createSelector(paymentMerchantManagePageState, paymentMerchantManagePage.getPaymentMerchants);
/*
* enlist-progress-page
* */
export const enlistProgressPageEnlistId = createSelector(progressPageState, progressPage.getEnlistId);
export const enlistProgressPageEnlists = createSelector(progressPageState, progressPage.getEnlists);
export const enlistProgressPageEnlist = createSelector(progressPageState, progressPage.getEnlist);
export const enlistProgressPageEnlistFeedbacks = createSelector(progressPageState, progressPage.getEnlistFeedbacks);
export const enlistProgressPageStateRightTabIndex = createSelector(progressPageState, progressPage.getRightTabIndex);
/*
* enlist-history-page
* */
export const historyPagePageSiz = createSelector(historyPageState, historyPage.getPageSiz);
export const historyPageCount = createSelector(historyPageState, historyPage.getCount);
export const historyPageTasks = createSelector(historyPageState, historyPage.getEnlists);
/*
* enlist-update-page
* */
export const enlistUpdatePageTitle = createSelector(updatePageState, updatePage.getTitle);
export const enlistUpdatePageEnlist = createSelector(updatePageState, updatePage.getEnlist);
