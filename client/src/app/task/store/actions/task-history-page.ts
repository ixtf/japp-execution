import {Action} from '@ngrx/store';
import {Task} from '../../../shared/models/task';

export const INIT = '[TaskHistoryPage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(public first: number, public pageSize: number) {
  }
}

export const INIT_SUCCESS = '[TaskHistoryPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public first: number, public pageSize: number, public count: number, public tasks: Task[]) {
  }
}

export type Actions
  = Init
  | InitSuccess;
