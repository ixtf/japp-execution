import {Action} from '@ngrx/store';
import {EnlistFeedback} from '../../../shared/models/enlist-feedback';

export const SAVE = '[EnlistFeedback] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public enlistId: string, public enlistFeedback: EnlistFeedback) {
  }
}

export const DELETE = '[EnlistFeedback] DELETE';

export class Delete implements Action {
  readonly type = DELETE;

  constructor(public enlistId: string, public enlistFeedbackId: string) {
  }
}

export const LIST = '[EnlistFeedback] LIST';

export class List implements Action {
  readonly type = LIST;

  constructor(public enlistId: string) {
  }
}

export const LIST_SUCCESS = '[EnlistFeedback] LIST_SUCCESS';

export class ListSuccess implements Action {
  readonly type = LIST_SUCCESS;

  constructor(public enlistId: string, public enlistFeedbacks: EnlistFeedback[]) {
  }
}

export type Actions
  = Save
  | List
  | ListSuccess
  | Delete;
