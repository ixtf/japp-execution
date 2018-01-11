import {Action} from '@ngrx/store';
import {Channel} from '../../../shared/models/channel';

export const INIT = '[AdminChannelMerchantManagePage] INIT';

export class Init implements Action {
  readonly type = INIT;
}

export const INIT_SUCCESS = '[AdminChannelMerchantManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public channels: Channel[]) {
  }
}

export type Actions
  = Init
  | InitSuccess;
