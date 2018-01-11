/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Task} from '../../../shared/models/task';

export const INIT = '[TaskUpdatePage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(public taskId?: string) {
  }
}

export const SAVE = '[TaskUpdatePage] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public payload: { task: Task }) {
  }
}

export type Actions
  = Init
  | Save;
