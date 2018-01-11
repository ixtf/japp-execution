import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as paymentMerchantManagePage from '../store/reducers/payment-merchant-manage-page';
import * as planAuditPage from '../store/reducers/plan-audit-page';
import * as channelManagePage from './reducers/channel-mange-page';

export * from './actions';

export interface AdminState {
  channelManagePage: channelManagePage.State;
  paymentMerchantManagePage: paymentMerchantManagePage.State;
  planAuditPage: planAuditPage.State;
}

export const reducers = {
  channelManagePage: channelManagePage.reducer,
  paymentMerchantManagePage: paymentMerchantManagePage.reducer,
  planAuditPage: planAuditPage.reducer,
};

export const featureName = 'admin';
export const adminState = createFeatureSelector<AdminState>(featureName);

export const channelManagePageState = createSelector(adminState, state => state.channelManagePage);

export const paymentMerchantManagePageState = createSelector(adminState, state => state.paymentMerchantManagePage);
export const paymentMerchantManagePagePaymentMerchants = createSelector(paymentMerchantManagePageState, paymentMerchantManagePage.getPaymentMerchants);

export const planAuditPageState = createSelector(adminState, state => state.planAuditPage);
export const planAuditPagePlans = createSelector(planAuditPageState, planAuditPage.getPlans);

