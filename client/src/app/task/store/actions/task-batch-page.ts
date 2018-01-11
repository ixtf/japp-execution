import {Params} from '@angular/router';
import {Action} from '@ngrx/store';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';
import {TaskOperatorContextData} from '../../../shared/models/task-operator-context-data';

export const INIT = '[TaskBatchPage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(public old: Params, public cur: Params) {
  }
}

export const INIT_SUCCESS = '[TaskBatchPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public taskGroups: TaskGroup[], public taskGroupId: string, public tasks: Task[]) {
  }
}

export const SET_TASK_GROUP_FILTER_NAMEQ = '[TaskBatchPage] SET_TASK_GROUP_FILTER_NAMEQ';

export class SetTaskGroupFilterNameQ implements Action {
  readonly type = SET_TASK_GROUP_FILTER_NAMEQ;

  constructor(public payload: string) {
  }
}

export const TO_DEST = '[TaskBatchPage] TO_DEST';

export class ToDest implements Action {
  readonly type = TO_DEST;

  constructor(public task: Task) {
  }
}

export const TO_DEST_ALL = '[TaskBatchPage] TO_DEST_ALL';

export class ToDestAll implements Action {
  readonly type = TO_DEST_ALL;
}

export const TO_SOURCE = '[TaskBatchPage] TO_SOURCE';

export class ToSource implements Action {
  readonly type = TO_SOURCE;

  constructor(public task: Task) {
  }
}

export const TO_SOURCE_ALL = '[TaskBatchPage] TO_SOURCE_ALL';

export class ToSourceAll implements Action {
  readonly type = TO_SOURCE_ALL;
}

export const TASKS_INVITE = '[TaskBatchPage] TASKS_INVITE';

export class TasksInvite implements Action {
  readonly type = TASKS_INVITE;

  constructor(public tasks: Task[]) {
  }
}

export const TASKS_FOLLOW_INVITE = '[TaskBatchPage] TASKS_FOLLOW_INVITE';

export class TasksFollowInvite implements Action {
  readonly type = TASKS_FOLLOW_INVITE;

  constructor(public tasks: Task[]) {
  }
}

export const UPDATE_NICKNAME = '[TaskBatchPage] UPDATE_NICKNAME';

export class UpdateNickname implements Action {
  readonly type = UPDATE_NICKNAME;

  constructor(public tasks: Task[], public operators: Operator[]) {
  }
}

export const UPDATE_NICKNAME_SUCCESS = '[TaskBatchPage] UPDATE_NICKNAME_SUCCESS';

export class UpdateNicknameSuccess implements Action {
  readonly type = UPDATE_NICKNAME_SUCCESS;

  constructor(public tasks: TaskOperatorContextData[], public operators: Operator[]) {
  }
}

export type Actions
  = Init
  | InitSuccess
  | SetTaskGroupFilterNameQ
  | ToDest
  | ToDestAll
  | ToSource
  | ToSourceAll;
