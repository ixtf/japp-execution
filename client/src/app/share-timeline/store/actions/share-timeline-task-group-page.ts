import {Params} from '@angular/router';
import {Action} from '@ngrx/store';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskEvaluate} from '../../../shared/models/task-evaluate';
import {TaskFeedback} from '../../../shared/models/task-feedback';
import {TaskGroup} from '../../../shared/models/task-group';
import {TaskOperatorContextData} from '../../../shared/models/task-operator-context-data';

export const INIT = '[ShareTimelineTaskGroupPage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(public old: Params, public cur: Params) {
  }
}

export const INIT_SUCCESS = '[ShareTimelineTaskGroupPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { shareOperator: Operator; taskObjects: { task: Task, taskEvaluates: TaskEvaluate[], taskFeedbacks: TaskFeedback[], taskOperatorContextDatas: TaskOperatorContextData[] }[]; taskGroup: TaskGroup }) {
  }
}

export type Actions
  = Init
  | InitSuccess;
