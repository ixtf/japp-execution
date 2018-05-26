import {Action} from '@ngrx/store';
import {Task} from '../../../shared/models/task';

export const INIT_SUCCESS = '[AdminTaskManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { tasks: Task[], count: number, first: number, pageSize: number }) {
  }
}

export type Actions =
  | InitSuccess;
