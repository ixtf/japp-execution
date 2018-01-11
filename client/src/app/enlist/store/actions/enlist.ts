import {Action} from '@ngrx/store';
import {Enlist} from '../../../shared/models/enlist';
import {TaskGroup} from '../../../shared/models/task-group';

export const SAVE = '[Enlist] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public enlist: Enlist) {
  }
}

export const CREATE_SUCCESS = '[Enlist] CREATE_SUCCESS';

export class CreateSuccess implements Action {
  readonly type = CREATE_SUCCESS;

  constructor(public enlist: Enlist) {
  }
}

export const UPDATE_SUCCESS = '[Enlist] UPDATE_SUCCESS';

export class UpdateSuccess implements Action {
  readonly type = UPDATE_SUCCESS;

  constructor(public enlist: Enlist) {
  }
}

export const GET = '[Enlist] GET';

export class Get implements Action {
  readonly type = GET;

  constructor(public enlistId: string) {
  }
}

export const GET_SUCCESS = '[Enlist] GET_SUCCESS';

export class GetSuccess implements Action {
  readonly type = GET_SUCCESS;

  constructor(public enlist: Enlist) {
  }
}

export const INVITE = '[EnlistProgressPage] INVITE';

export class Invite implements Action {
  readonly type = INVITE;

  constructor(public id: string) {
  }
}

export const DONE = '[Enlist] DONE';

export class Done implements Action {
  readonly type = DONE;

  constructor(public enlistId: string) {
  }
}

export const DONE_SUCCESS = '[Enlist] DONE_SUCCESS';

export class DoneSuccess implements Action {
  readonly type = DONE_SUCCESS;

  constructor(public enlistId: string) {
  }
}

export const DELETE_PARTICIPANT = '[Enlist] DELETE_PARTICIPANT';

export class DeleteParticipant implements Action {
  readonly type = DELETE_PARTICIPANT;

  constructor(public payload: { enlistId: string, participantId: string }) {
  }
}

export const DELETE_MANAGER = '[Enlist] DELETE_MANAGER';

export class DeleteManager implements Action {
  readonly type = DELETE_MANAGER;

  constructor(public payload: { enlistId: string, managerId: string }) {
  }
}

export const DELETE = '[Enlist] DELETE';

export class Delete implements Action {
  readonly type = DELETE;

  constructor(public enlistId: string) {
  }
}

export const DELETE_SUCCESS = '[Enlist] DELETE_SUCCESS';

export class DeleteSuccess implements Action {
  readonly type = DELETE_SUCCESS;

  constructor(public enlistId: string) {
  }
}

export const RESTART = '[Enlist] RESTART';

export class Restart implements Action {
  readonly type = RESTART;

  constructor(public enlistId: string) {
  }
}

export const RESTART_SUCCESS = '[Enlist] RESTART_SUCCESS';

export class RestartSuccess implements Action {
  readonly type = RESTART_SUCCESS;

  constructor(public enlistId: string) {
  }
}

export const GENERATE_TASK = '[Enlist] GENERATE_TASK';

export class GenerateTask implements Action {
  readonly type = GENERATE_TASK;

  constructor(public payload: { enlistId: string, taskGroup?: TaskGroup, taskGroupName?: string }) {
  }
}

export type Actions
  = Save
  | CreateSuccess
  | UpdateSuccess
  | GenerateTask
  | Get
  | GetSuccess
  | Invite
  | Done
  | DoneSuccess
  | DeleteParticipant
  | DeleteManager
  | Delete
  | DeleteSuccess
  | Restart
  | RestartSuccess;
