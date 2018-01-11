import {Action} from '@ngrx/store';
import {Operator} from '../../../shared/models/operator';

export const INIT = '[PersonalCenterPage] INIT';

export class Init implements Action {
  readonly type = INIT;
}

export const INIT_SUCCESS = '[PersonalCenterPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public operator: Operator) {
  }
}

export const UPDATE = '[PersonalCenterPage] UPDATE';

export class Update implements Action {
  readonly type = UPDATE;

  constructor(public payload: { name: string, mobile?: string }) {
  }
}

export const UPDATE_SUCCESS = '[PersonalCenterPage] UPDATE_SUCCESS';

export class UpdateSuccess implements Action {
  readonly type = UPDATE_SUCCESS;

  constructor(public payload: { name: string, mobile?: string }) {
  }
}

export const UPDATE_PASSWORD = '[PersonalCenterPage] UPDATE_PASSWORD';

export class UpdatePassword implements Action {
  readonly type = UPDATE_PASSWORD;

  constructor(public payload: { oldPassword: string, loginPassword: string, loginPasswordAgain: string }) {
  }
}

export type Actions
  = Init
  | InitSuccess
  | UpdatePassword
  | Update
  | UpdateSuccess;
