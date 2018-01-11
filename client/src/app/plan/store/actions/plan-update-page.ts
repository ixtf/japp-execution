/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Plan} from '../../../shared/models/plan';

export const INIT_SUCCESS = '[PlanUpdatePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public plan: Plan) {
  }
}

export const SAVE = '[PlanUpdatePage] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public plan: Plan) {
  }
}

export type Actions =
  | InitSuccess
  | Save;
