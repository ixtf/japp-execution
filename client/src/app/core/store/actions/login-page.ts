import {Action} from '@ngrx/store';
import {Authenticate, AuthenticateResponse} from '../../../shared/models/authenticate';

export const LOGIN = '[LoginPage] Login';

export class Login implements Action {
  readonly type = LOGIN;

  constructor(public authenticate: Authenticate) {
  }
}

export const LOGIN_SUCCESS = '[LoginPage] Login Success';

export class LoginSuccess implements Action {
  readonly type = LOGIN_SUCCESS;

  constructor(public payload: AuthenticateResponse) {
  }
}

export type Actions
  = Login
  | LoginSuccess;
