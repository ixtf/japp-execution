import {Action} from '@ngrx/store';
import {TaskFeedback} from '../../../shared/models/task-feedback';

export const SAVE = '[TaskFeedback] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public taskId: string, public taskFeedback: TaskFeedback) {
  }
}

export const DELETE = '[TaskFeedback] DELETE';

export class Delete implements Action {
  readonly type = DELETE;

  constructor(public taskId: string, public taskFeedbackId: string) {
  }
}

export const LIST = '[TaskFeedback] LIST';

export class List implements Action {
  readonly type = LIST;

  constructor(public taskId: string) {
  }
}

export const LIST_SUCCESS = '[TaskFeedback] LIST_SUCCESS';

export class ListSuccess implements Action {
  readonly type = LIST_SUCCESS;

  constructor(public taskId: string, public taskFeedbacks: TaskFeedback[]) {
  }
}

export type Actions
  = Save
  | List
  | ListSuccess
  | Delete;
