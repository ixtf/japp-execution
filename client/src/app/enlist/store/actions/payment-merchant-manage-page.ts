import {Action} from '@ngrx/store';
import {PaymentMerchant} from '../../../shared/models/payment-merchant';

export const INIT = '[PaymentMerchantManagePage] INIT';

export class Init implements Action {
  readonly type = INIT;
}

export const INIT_SUCCESS = '[PaymentMerchantManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public paymentMerchants: PaymentMerchant[]) {
  }
}

export const INVITE = '[PaymentMerchantManagePage] INVITE';

export class Invite implements Action {
  readonly type = INVITE;

  constructor(public paymentMerchantId: string) {
  }
}

export const DELETE_MANAGER = '[PaymentMerchantManagePage] DELETE_MANAGER';

export class DeleteManager implements Action {
  readonly type = DELETE_MANAGER;

  constructor(public payload: { paymentMerchantId: string, managerId: string }) {
  }
}

export const DELETE_MANAGER_SUCCESS = '[PaymentMerchantManagePage] DELETE_MANAGER_SUCCESS';

export class DeleteManagerSuccess implements Action {
  readonly type = DELETE_MANAGER_SUCCESS;

  constructor(public payload: { paymentMerchantId: string, managerId: string }) {
  }
}

export type Actions
  = Init
  | DeleteManager
  | DeleteManagerSuccess
  | InitSuccess;
