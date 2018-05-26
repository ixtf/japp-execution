import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {Task} from '../../../shared/models/task';
import {TaskComplain} from '../../../shared/models/task-complain';
import {taskComplainHandlePageActions} from '../actions';

export interface State {
  taskComplainEntities: { [id: string]: TaskComplain };
  task: Task;
  initTaskComplainId: string;
}

export const initialState: State = {
  taskComplainEntities: {},
  task: null,
  initTaskComplainId: null,
};

export function reducer(state = initialState, action: taskComplainHandlePageActions.Actions): State {
  switch (action.type) {
    case taskComplainHandlePageActions.INIT_SUCCESS: {
      const {task, taskComplains, initTaskComplainId} = action.payload;
      const taskComplainEntities = TaskComplain.toEntities(taskComplains);
      return {...state, task, taskComplainEntities, initTaskComplainId};
    }
    default: {
      return state;
    }
  }
}

export const getTask = (state: State) => state.task;
export const getTaskComplainEntities = (state: State) => state.taskComplainEntities;
export const getInitTaskComplainId = (state: State) => state.initTaskComplainId;
export const getInitTaskComplain = createSelector(getTaskComplainEntities, getInitTaskComplainId, (entities, id) => entities[id]);
export const getTaskComplains = createSelector(getTaskComplainEntities, (entities) => {
  return Object.keys(entities)
    .map(it => entities[it])
    .sort(DefaultCompare);
});
