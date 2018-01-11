/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Plan} from '../../../shared/models/plan';

export const INIT_SUCCESS = '[PlanManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { first: number, pageSize: number, count: number, draft: boolean, published: boolean, audited: boolean, plans: Plan[] }) {
  }
}

export type Actions =
  | InitSuccess;
