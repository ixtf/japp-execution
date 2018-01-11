/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {TaskGroup} from '../../../shared/models/task-group';

export const INIT = '[TaskGroupUpdatePage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(public id?: string) {
  }
}

export const INIT_SUCCESS = '[TaskGroupUpdatePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public taskGroup: TaskGroup) {
  }
}

export const SAVE = '[TaskGroupUpdatePage] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public taskGroup: TaskGroup) {
  }
}

export type Actions
  = Init
  | InitSuccess
  | Save;
