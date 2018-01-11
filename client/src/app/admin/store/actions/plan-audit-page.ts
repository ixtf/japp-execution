import {Action} from '@ngrx/store';
import {Plan} from '../../../shared/models/plan';

export const INIT_SUCCESS = '[AdminPlanAuditPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { first: number, pageSize: number, count: number, plans: Plan[] }) {
  }
}

export const AUDIT_SUCCESS = '[AdminPlanAuditPage] AUDIT_SUCCESS';

export class AuditSuccess implements Action {
  readonly type = AUDIT_SUCCESS;

  constructor(public payload: { plan: Plan }) {
  }
}

export const UNAUDIT_SUCCESS = '[AdminPlanAuditPage] UNAUDIT_SUCCESS';

export class UnAuditSuccess implements Action {
  readonly type = UNAUDIT_SUCCESS;

  constructor(public payload: { plan: Plan }) {
  }
}

export type Actions =
  | AuditSuccess
  | UnAuditSuccess
  | InitSuccess;
