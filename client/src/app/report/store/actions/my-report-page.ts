/**
 * Created by jzb on 17-6-6.
 */
import {Action} from '@ngrx/store';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';

export const INIT = '[MyReportPage] INIT';

export class Init implements Action {
  readonly type = INIT;

  constructor(t?: string) {
  }
}

export const INIT_SUCCESS = '[MyReportPage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { tasks: Task[], taskGroups: TaskGroup[] }) {
  }
}

export const SET_TASKFILTER_ROLE = '[MyReportPage] SET_TASKFILTER_ROLE';

export class SetTaskFilterRole implements Action {
  readonly type = SET_TASKFILTER_ROLE;

  constructor(public payload: string | number) {
  }
}

export const SET_TASKFILTER_STATUS = '[MyReportPage] SET_TASKFILTER_STATUS';

export class SetTaskFilterStatus implements Action {
  readonly type = SET_TASKFILTER_STATUS;

  constructor(public payload: string | number) {
  }
}

export type Actions
  = Init
  | InitSuccess
  | SetTaskFilterStatus
  | SetTaskFilterRole;
