import {EnlistFeedbackEffects} from './enlist-feedback.effects';
import {EnlistHistoryPageEffect} from './enlist-history-page.effect';
import {EnlistProgressPageEffects} from './enlist-progress-page.effects';
import {EnlistUpdatePageEffects} from './enlist-update-page.effects';
import {EnlistEffects} from './enlist.effects';
import {PaymentMerchantManagePageEffects} from './payment-merchant-manage-page.effects';

export const featureEffects = [
  PaymentMerchantManagePageEffects,
  EnlistEffects,
  EnlistFeedbackEffects,
  EnlistUpdatePageEffects,
  EnlistProgressPageEffects,
  EnlistHistoryPageEffect,
];
