import {Operator} from './operator';

export interface Authenticate {
  loginId: string;
  loginPassword: string;
}

export interface AuthenticateResponse {
  isAdmin: boolean;
  operator: Operator;
}
