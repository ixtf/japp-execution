import {Action} from '@ngrx/store';
import {TaskEvaluate} from '../../../shared/models/task-evaluate';

export const SAVE = '[TaskEvaluate] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public taskId: string, public taskEvaluate: TaskEvaluate) {
  }
}

export const DELETE = '[TaskEvaluate] DELETE';

export class Delete implements Action {
  readonly type = DELETE;

  constructor(public taskId: string, public taskEvaluateId: string) {
  }
}

export const LIST = '[TaskEvaluate] LIST';

export class List implements Action {
  readonly type = LIST;

  constructor(public taskId: string) {
  }
}

export const LIST_SUCCESS = '[TaskEvaluate] LIST_SUCCESS';

export class ListSuccess implements Action {
  readonly type = LIST_SUCCESS;

  constructor(public taskId: string, public taskEvaluates: TaskEvaluate[]) {
  }
}

export type Actions
  = Save
  | List
  | ListSuccess
  | Delete;
