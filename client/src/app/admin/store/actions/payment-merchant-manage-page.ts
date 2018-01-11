import {Action} from '@ngrx/store';
import {PaymentMerchant} from '../../../shared/models/payment-merchant';

export const INIT = '[AdminPaymentMerchantManagePage] INIT';

export class Init implements Action {
  readonly type = INIT;
}

export const INIT_SUCCESS = '[AdminPaymentMerchantManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public paymentMerchants: PaymentMerchant[]) {
  }
}

export const INVITE = '[AdminPaymentMerchantManagePage] INVITE';

export class Invite implements Action {
  readonly type = INVITE;

  constructor(public paymentMerchantId: string) {
  }
}

export type Actions
  = Init
  | InitSuccess
  | Invite;
