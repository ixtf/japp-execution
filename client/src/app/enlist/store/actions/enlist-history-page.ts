import {Action} from '@ngrx/store';
import {Enlist} from '../../../shared/models/enlist';

export const LIST = '[EnlistHistoryPage] LIST';

export class List implements Action {
  readonly type = LIST;

  constructor(public first: number, public pageSize: number) {
  }
}

export const LIST_SUCCESS = '[EnlistHistoryPage] LIST_SUCCESS';

export class ListSuccess implements Action {
  readonly type = LIST_SUCCESS;

  constructor(public first: number, public pageSize: number, public count: number, public enlists: Enlist[]) {
  }
}

export type Actions
  = List
  | ListSuccess;
