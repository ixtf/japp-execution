import {Action} from '@ngrx/store';
import {TaskNotice} from '../../../shared/models/task-notice';

export const SAVE = '[TaskNotice] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public taskId: string, public taskNotice: TaskNotice) {
  }
}

export const CREATE_SUCCESS = '[TaskNotice] CREATE_SUCCESS';

export class CreateSuccess implements Action {
  readonly type = CREATE_SUCCESS;

  constructor(public taskId: string, public taskNotice: TaskNotice) {
  }
}

export const UPDATE_SUCCESS = '[TaskNotice] UPDATE_SUCCESS';

export class UpdateSuccess implements Action {
  readonly type = UPDATE_SUCCESS;

  constructor(public taskId: string, public taskNotice: TaskNotice) {
  }
}

export const DELETE = '[TaskNotice] DELETE';

export class Delete implements Action {
  readonly type = DELETE;

  constructor(public taskId: string, public taskNoticeId: string) {
  }
}

export const DELETE_SUCCESS = '[TaskNotice] DELETE_SUCCESS';

export class DeleteSuccess implements Action {
  readonly type = DELETE_SUCCESS;

  constructor(public taskId: string, public taskNoticeId: string) {
  }
}

export const LIST = '[TaskNotice] LIST';

export class List implements Action {
  readonly type = LIST;

  constructor(public taskId: string) {
  }
}

export const LIST_SUCCESS = '[TaskNotice] LIST_SUCCESS';

export class ListSuccess implements Action {
  readonly type = LIST_SUCCESS;

  constructor(public taskId: string, public taskNotices: TaskNotice[]) {
  }
}

export type Actions
  = Save
  | CreateSuccess
  | UpdateSuccess
  | List
  | ListSuccess
  | Delete
  | DeleteSuccess;
