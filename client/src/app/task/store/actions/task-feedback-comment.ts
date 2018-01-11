import {Action} from '@ngrx/store';
import {TaskFeedbackComment} from '../../../shared/models/task-feedback-comment';

export const SAVE = '[TaskFeedbackComment] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public taskId: string, public taskFeedbackId: string, public taskFeedbackComment: TaskFeedbackComment) {
  }
}

export const DELETE = '[TaskFeedbackComment] DELETE';

export class Delete implements Action {
  readonly type = DELETE;

  constructor(public taskId: string, public taskFeedbackId: string, public taskFeedbackCommentId: string) {
  }
}

export const LIST = '[TaskFeedbackComment] LIST';

export class List implements Action {
  readonly type = LIST;

  constructor(public taskId: string, public taskFeedbackId: string) {
  }
}

export const LIST_SUCCESS = '[TaskFeedbackComment] LIST_SUCCESS';

export class ListSuccess implements Action {
  readonly type = LIST_SUCCESS;

  constructor(public taskId: string, public taskFeedbackId: string, public taskFeedbackComments: TaskFeedbackComment[]) {
  }
}

export type Actions
  = Save
  | List
  | ListSuccess
  | Delete;
