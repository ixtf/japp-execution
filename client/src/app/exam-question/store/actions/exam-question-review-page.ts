import {Params} from '@angular/router';
import {Action} from '@ngrx/store';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';

export const INIT = '[ExamQuestionReviewPage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(public payload: { old: Params; cur: Params; }) {
  }
}

export const INIT_SUCCESS = '[ExamQuestionReviewPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public taskGroups: TaskGroup[], public taskGroupId: string, public tasks: Task[], public isManage: boolean) {
  }
}

export const SET_TASK_GROUP_FILTER_NAMEQ = '[ExamQuestionReviewPage] SET_TASK_GROUP_FILTER_NAMEQ';

export class SetTaskGroupFilterNameQ implements Action {
  readonly type = SET_TASK_GROUP_FILTER_NAMEQ;

  constructor(public payload: string) {
  }
}

export const TO_DEST = '[ExamQuestionReviewPage] TO_DEST';

export class ToDest implements Action {
  readonly type = TO_DEST;

  constructor(public task: Task) {
  }
}

export const TO_DEST_ALL = '[ExamQuestionReviewPage] TO_DEST_ALL';

export class ToDestAll implements Action {
  readonly type = TO_DEST_ALL;
}

export const TO_SOURCE = '[ExamQuestionReviewPage] TO_SOURCE';

export class ToSource implements Action {
  readonly type = TO_SOURCE;

  constructor(public task: Task) {
  }
}

export const TO_SOURCE_ALL = '[ExamQuestionReviewPage] TO_SOURCE_ALL';

export class ToSourceAll implements Action {
  readonly type = TO_SOURCE_ALL;
}

export const JOIN = '[ExamQuestionReviewPage] JOIN';

export class Join implements Action {
  readonly type = JOIN;

  constructor(public payload: { isManage: boolean; tasks: Task[]; operatorId?: string; fileName?: string }) {
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
