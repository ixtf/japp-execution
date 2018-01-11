import {Action} from '@ngrx/store';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskContextData} from '../../../shared/models/task-context-data';
import {TaskOperatorContextData} from '../../../shared/models/task-operator-context-data';

export const SAVE = '[Task] SAVE';

export class Save implements Action {
  readonly type = SAVE;

  constructor(public task: Task) {
  }
}

export const CREATE_SUCCESS = '[Task] CREATE_SUCCESS';

export class CreateSuccess implements Action {
  readonly type = CREATE_SUCCESS;

  constructor(public task: Task) {
  }
}

export const UPDATE_SUCCESS = '[Task] UPDATE_SUCCESS';

export class UpdateSuccess implements Action {
  readonly type = UPDATE_SUCCESS;

  constructor(public task: Task) {
  }
}

export const GET = '[Task] GET';

export class Get implements Action {
  readonly type = GET;

  constructor(public id: string) {
  }
}

export const GET_SUCCESS = '[Task] GET_SUCCESS';

export class GetSuccess implements Action {
  readonly type = GET_SUCCESS;

  constructor(public task: Task) {
  }
}

export const TOP = '[Task] TOP';

export class Top implements Action {
  readonly type = TOP;

  constructor(public task: Task) {
  }
}

export const COPY = '[Task] COPY';

export class Copy implements Action {
  readonly type = COPY;

  constructor(public taskId: string) {
  }
}

export const READ = '[Task] READ';

export class Read implements Action {
  readonly type = READ;

  constructor(public taskId: string) {
  }
}

export const READ_SUCCESS = '[Task] READ_SUCCESS';

export class ReadSuccess implements Action {
  readonly type = READ_SUCCESS;

  constructor(public taskId: string) {
  }
}

export const DONE = '[Task] DONE';

export class Done implements Action {
  readonly type = DONE;

  constructor(public taskId: string) {
  }
}

export const DONE_SUCCESS = '[Task] DONE_SUCCESS';

export class DoneSuccess implements Action {
  readonly type = DONE_SUCCESS;

  constructor(public taskId: string) {
  }
}

export const QUIT = '[Task] QUIT';

export class Quit implements Action {
  readonly type = QUIT;

  constructor(public taskId: string) {
  }
}

export const QUIT_SUCCESS = '[Task] QUIT_SUCCESS';

export class QuitSuccess implements Action {
  readonly type = QUIT_SUCCESS;

  constructor(public taskId: string) {
  }
}

export const DELETE = '[Task] DELETE';

export class Delete implements Action {
  readonly type = DELETE;

  constructor(public taskId: string) {
  }
}

export const DELETE_SUCCESS = '[Task] DELETE_SUCCESS';

export class DeleteSuccess implements Action {
  readonly type = DELETE_SUCCESS;

  constructor(public taskId: string) {
  }
}

export const RESTART = '[Task] RESTART';

export class Restart implements Action {
  readonly type = RESTART;

  constructor(public taskId: string) {
  }
}

export const RESTART_SUCCESS = '[Task] RESTART_SUCCESS';

export class RestartSuccess implements Action {
  readonly type = RESTART_SUCCESS;

  constructor(public taskId: string) {
  }
}

export const DELETE_FOLLOWER = '[Task] DELETE_FOLLOWER';

export class DeleteFollower implements Action {
  readonly type = DELETE_FOLLOWER;

  constructor(public taskId: string, public followerId: string) {
  }
}

export const DELETE_PARTICIPANT = '[Task] DELETE_PARTICIPANT';

export class DeleteParticipant implements Action {
  readonly type = DELETE_PARTICIPANT;

  constructor(public taskId: string, public participantId: string) {
  }
}

export const IMPORT_FOLLOWERS = '[Task] IMPORT_FOLLOWERS';

export class ImportFollowers implements Action {
  readonly type = IMPORT_FOLLOWERS;

  constructor(public taskId: string, public followers: Operator[]) {
  }
}

export const IMPORT_PARTICIPANTS = '[Task] IMPORT_PARTICIPANTS';

export class ImportParticipants implements Action {
  readonly type = IMPORT_PARTICIPANTS;

  constructor(public taskId: string, public participants: Operator[]) {
  }
}

export const UPDATE_NICKNAME = '[Task] UPDATE_NICKNAME';

export class UpdateNickname implements Action {
  readonly type = UPDATE_NICKNAME;

  constructor(public taskId: string, public operatorId, public nickName: string) {
  }
}

export const UPDATE_NICKNAME_SUCCESS = '[Task] UPDATE_NICKNAME_SUCCESS';

export class UpdateNicknameSuccess implements Action {
  readonly type = UPDATE_NICKNAME_SUCCESS;

  constructor(public taskId: string, public operatorId, public nickName: string) {
  }
}

export const GET_CONTEXT_DATA = '[Task] GET_CONTEXT_DATA';

export class GetContextData implements Action {
  readonly type = GET_CONTEXT_DATA;

  constructor(public taskId: string) {
  }
}

export const GET_CONTEXT_DATA_SUCCESS = '[Task] GET_CONTEXT_DATA_SUCCESS';

export class GetContextDataSuccess implements Action {
  readonly type = GET_CONTEXT_DATA_SUCCESS;

  constructor(public taskId: string, public taskContextData: TaskContextData) {
  }
}

export const GET_OPERATOR_CONTEXT_DATA = '[Task] GET_OPERATOR_CONTEXT_DATA';

export class GetOperatorContextData implements Action {
  readonly type = GET_OPERATOR_CONTEXT_DATA;

  constructor(public taskId: string) {
  }
}

export const GET_OPERATOR_CONTEXT_DATA_SUCCESS = '[Task] GET_OPERATOR_CONTEXT_DATA_SUCCESS';

export class GetOperatorContextDataSuccess implements Action {
  readonly type = GET_OPERATOR_CONTEXT_DATA_SUCCESS;

  constructor(public taskId: string, public taskOperatorContextData: TaskOperatorContextData) {
  }
}

export type Actions
  = Save
  | CreateSuccess
  | UpdateSuccess
  | GetContextData
  | GetContextDataSuccess
  | GetOperatorContextData
  | GetOperatorContextDataSuccess
  | Top
  | Copy
  | Read
  | ReadSuccess
  | Get
  | GetSuccess
  | Done
  | DoneSuccess
  | Quit
  | QuitSuccess
  | Delete
  | DeleteSuccess
  | Restart
  | RestartSuccess
  | DeleteFollower
  | DeleteParticipant
  | ImportFollowers
  | ImportParticipants
  | UpdateNickname
  | UpdateNicknameSuccess;
