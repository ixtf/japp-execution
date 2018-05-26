import {createFeatureSelector, createSelector} from '@ngrx/store';
import * as paymentMerchantManagePage from '../store/reducers/payment-merchant-manage-page';
import * as redEnvelopeOrganizationManagePage from '../store/reducers/red-envelope-organization-manage-page';
import * as redEnvelopeOrganizationUpdatePage from '../store/reducers/red-envelope-organization-update-page';
import * as planAuditPage from '../store/reducers/plan-audit-page';
import * as channelManagePage from './reducers/channel-mange-page';
import * as taskManagePage from './reducers/task-mange-page';
import * as taskComplainHandlePage from './reducers/task-complain-handle-page';

export * from './actions';

export interface AdminState {
  channelManagePage: channelManagePage.State;
  paymentMerchantManagePage: paymentMerchantManagePage.State;
  planAuditPage: planAuditPage.State;
  taskManagePage: taskManagePage.State;
  taskComplainHandlePage: taskComplainHandlePage.State;
  redEnvelopeOrganizationManagePage: redEnvelopeOrganizationManagePage.State;
  redEnvelopeOrganizationUpdatePage: redEnvelopeOrganizationUpdatePage.State;
}

export const reducers = {
  channelManagePage: channelManagePage.reducer,
  paymentMerchantManagePage: paymentMerchantManagePage.reducer,
  planAuditPage: planAuditPage.reducer,
  taskManagePage: taskManagePage.reducer,
  taskComplainHandlePage: taskComplainHandlePage.reducer,
  redEnvelopeOrganizationManagePage: redEnvelopeOrganizationManagePage.reducer,
  redEnvelopeOrganizationUpdatePage: redEnvelopeOrganizationUpdatePage.reducer,
};

export const featureName = 'admin';
export const adminState = createFeatureSelector<AdminState>(featureName);
export const channelManagePageState = createSelector(adminState, state => state.channelManagePage);
export const paymentMerchantManagePageState = createSelector(adminState, state => state.paymentMerchantManagePage);
export const planAuditPageState = createSelector(adminState, state => state.planAuditPage);
export const taskManagePageState = createSelector(adminState, state => state.taskManagePage);
export const taskComplainHandlePageState = createSelector(adminState, state => state.taskComplainHandlePage);
export const redEnvelopeOrganizationUpdatePageState = createSelector(adminState, state => state.redEnvelopeOrganizationUpdatePage);
export const redEnvelopeOrganizationManagePageState = createSelector(adminState, state => state.redEnvelopeOrganizationManagePage);
export const paymentMerchantManagePagePaymentMerchants = createSelector(paymentMerchantManagePageState, paymentMerchantManagePage.getPaymentMerchants);

export const planAuditPagePlans = createSelector(planAuditPageState, planAuditPage.getPlans);

export const taskManagePageTasks = createSelector(taskManagePageState, taskManagePage.getTasks);
export const taskManagePageCount = createSelector(taskManagePageState, taskManagePage.getCount);
export const taskManagePagePageSize = createSelector(taskManagePageState, taskManagePage.getPageSize);

export const taskComplainHandlePageTask = createSelector(taskComplainHandlePageState, taskComplainHandlePage.getTask);
export const taskComplainHandlePageTaskComplains = createSelector(taskComplainHandlePageState, taskComplainHandlePage.getTaskComplains);
export const taskComplainHandlePageInitTaskComplain = createSelector(taskComplainHandlePageState, taskComplainHandlePage.getInitTaskComplain);

