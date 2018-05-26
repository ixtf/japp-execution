import {Action} from '@ngrx/store';
import {Task} from '../../../shared/models/task';
import {TaskComplain} from '../../../shared/models/task-complain';

export const INIT_SUCCESS = '[AdminTaskComplainHandlePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public payload: { task: Task, taskComplains: TaskComplain[], initTaskComplainId: string }) {
  }
}

export type Actions =
  | InitSuccess;
