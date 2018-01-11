import {Params} from '@angular/router';
import {Action} from '@ngrx/store';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskEvaluate} from '../../../shared/models/task-evaluate';
import {TaskFeedback} from '../../../shared/models/task-feedback';
import {TaskOperatorContextData} from '../../../shared/models/task-operator-context-data';

export const INIT = '[ShareTimelineTaskPage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(public old: Params, public cur: Params) {
  }
}

export const INIT_SUCCESS = '[ShareTimelineTaskPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { shareOperator: Operator; task: Task; taskFeedbacks: TaskFeedback[]; taskEvaluates: TaskEvaluate[]; taskOperatorContextDatas: TaskOperatorContextData[] }) {
  }
}

export const SET_RIGHT_TAB_INDEX = '[ShareTimelineTaskPage] SET_RIGHT_TAB_INDEX';

export class SetRightTabIndex implements Action {
  readonly type = SET_RIGHT_TAB_INDEX;

  constructor(public payload: number) {
  }
}

export type Actions
  = Init
  | InitSuccess
  | SetRightTabIndex;
