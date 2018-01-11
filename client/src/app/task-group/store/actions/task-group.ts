/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {TaskGroup} from '../../../shared/models/task-group';

export const GET = '[TaskGroup] GET';

export class Get implements Action {
  readonly type = GET;

  constructor(public id: string) {
  }
}

export const GET_SUCCESS = '[TaskGroup] GET_SUCCESS';

export class GetSuccess implements Action {
  readonly type = GET_SUCCESS;

  constructor(public taskGroup: TaskGroup) {
  }
}

export const CREATE_SUCCESS = '[TaskGroup] CREATE_SUCCESS';

export class CreateSuccess implements Action {
  readonly type = CREATE_SUCCESS;

  constructor(public taskGroup: TaskGroup) {
  }
}

export const UPDATE_SUCCESS = '[TaskGroup] UPDATE_SUCCESS';

export class UpdateSuccess implements Action {
  readonly type = UPDATE_SUCCESS;

  constructor(public taskGroup: TaskGroup) {
  }
}

export const TOP = '[TaskGroup] TOP';

export class Top implements Action {
  readonly type = TOP;

  constructor(public taskGroup: TaskGroup) {
  }
}

export type Actions
  = Get
  | GetSuccess
  | Top
  | UpdateSuccess
  | CreateSuccess;
