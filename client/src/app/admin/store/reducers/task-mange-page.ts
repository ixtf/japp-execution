import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {Task} from '../../../shared/models/task';
import {taskManagePageActions} from '../actions';

export interface State {
  taskEntities: { [id: string]: Task };
  count: number;
  first: number;
  pageSize: number;
}

export const initialState: State = {
  taskEntities: {},
  count: 0,
  first: 0,
  pageSize: 0,
};

export function reducer(state = initialState, action: taskManagePageActions.Actions): State {
  switch (action.type) {
    case taskManagePageActions.INIT_SUCCESS: {
      const {first, count, pageSize, tasks} = action.payload;
      const taskEntities = Task.toEntities(tasks);
      return {...state, first, count, pageSize, taskEntities};
    }
    default: {
      return state;
    }
  }
}

export const getCount = (state: State) => state.count;
export const getFirst = (state: State) => state.first;
export const getPageSize = (state: State) => state.pageSize;
export const getTaskEntities = (state: State) => state.taskEntities;
export const getTasks = createSelector(getTaskEntities, (entities) => {
  return Object.keys(entities)
    .map(it => entities[it])
    .sort(DefaultCompare);
});
