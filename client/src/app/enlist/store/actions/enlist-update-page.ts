/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Enlist} from '../../../shared/models/enlist';

export const INIT = '[EnlistUpdatePage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(public payload: { paymentMerchantId?: string, enlistId?: string }) {
  }
}

export const INIT_SUCCESS = '[EnlistUpdatePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public enlist: Enlist) {
  }
}

export const SAVE = '[EnlistUpdatePage] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public enlist: Enlist) {
  }
}


export type Actions
  = Init
  | InitSuccess
  | Save;
