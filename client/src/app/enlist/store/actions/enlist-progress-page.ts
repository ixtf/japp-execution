import {Params} from '@angular/router';
import {Action} from '@ngrx/store';
import {Enlist} from '../../../shared/models/enlist';

export const INIT = '[EnlistProgressPage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(public old: Params, public cur: Params) {
  }
}

export const INIT_SUCCESS = '[EnlistProgressPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public enlists: Enlist[], public enlistId: string) {
  }
}

export const SET_RIGHT_TAB_INDEX = '[EnlistProgressPage] SET_RIGHT_TAB_INDEX';

export class SetRightTabIndex implements Action {
  readonly type = SET_RIGHT_TAB_INDEX;

  constructor(public payload: number) {
  }
}

export type Actions
  = Init
  | SetRightTabIndex
  | InitSuccess;
