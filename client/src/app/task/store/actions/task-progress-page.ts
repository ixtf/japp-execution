import {Params} from '@angular/router';
import {Action} from '@ngrx/store';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';

export const INIT = '[TaskProgressPage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(public old: Params, public cur: Params) {
  }
}

export const INIT_SUCCESS = '[TaskProgressPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public taskGroups: TaskGroup[], public taskGroupId: string, public tasks: Task[], public taskId: string) {
  }
}

export const SET_TASK_GROUP_FILTER_NAMEQ = '[TaskProgressPage] SET_TASK_GROUP_FILTER_NAMEQ';

export class SetTaskGroupFilterNameQ implements Action {
  readonly type = SET_TASK_GROUP_FILTER_NAMEQ;

  constructor(public payload: string) {
  }
}

export const SET_TASK_FILTER = '[TaskProgressPage] SET_TASK_FILTER';

export class SetTaskFilter implements Action {
  readonly type = SET_TASK_FILTER;

  constructor(public payload: (task: Task) => boolean) {
  }
}

export const SET_TASK_SORT = '[TaskProgressPage] SET_TASK_SORT';

export class SetTaskSort implements Action {
  readonly type = SET_TASK_SORT;

  constructor(public payload: (o1: Task, o2: Task) => number) {
  }
}

export const SET_FEEDBACK_FILTER_NAMEQ = '[TaskProgressPage] SET_FEEDBACK_FILTER_NAMEQ';

export class SetFeedbackFilterNameQ implements Action {
  readonly type = SET_FEEDBACK_FILTER_NAMEQ;

  constructor(public payload: string) {
  }
}

export const SET_FEEDBACK_FILTER_NOCOMMENT = '[TaskProgressPage] SET_FEEDBACK_FILTER_NOCOMMENT';

export class SetFeedbackFilterNoComment implements Action {
  readonly type = SET_FEEDBACK_FILTER_NOCOMMENT;

  constructor(public payload?: boolean) {
  }
}

export const SET_EVALUATE_FILTER_NAMEQ = '[TaskProgressPage] SET_EVALUATE_FILTER_NAMEQ';

export class SetEvaluateFilterNameQ implements Action {
  readonly type = SET_EVALUATE_FILTER_NAMEQ;

  constructor(public payload: string) {
  }
}

export const SET_RIGHT_TAB_INDEX = '[TaskProgressPage] SET_RIGHT_TAB_INDEX';

export class SetRightTabIndex implements Action {
  readonly type = SET_RIGHT_TAB_INDEX;

  constructor(public payload: number) {
  }
}

export type Actions
  = Init
  | InitSuccess
  | SetTaskGroupFilterNameQ
  | SetTaskFilter
  | SetTaskSort
  | SetFeedbackFilterNameQ
  | SetFeedbackFilterNoComment
  | SetRightTabIndex
  | SetEvaluateFilterNameQ;
