import {Action} from '@ngrx/store';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';

export const INIT_SUCCESS = '[ShareTimelineTaskClockPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { shareOperator: Operator; taskId: string, tasks: Task[]; }) {
  }
}

export type Actions
  = InitSuccess;
