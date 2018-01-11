/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {TaskGroup} from '../../../shared/models/task-group';

export const INIT = '[TaskGroupManagePage] INIT';

export class Init implements Action {
  readonly type = INIT;
}

export const INIT_SUCCESS = '[TaskGroupManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public taskGroups: TaskGroup[]) {
  }
}

export type Actions
  = Init
  | InitSuccess;
