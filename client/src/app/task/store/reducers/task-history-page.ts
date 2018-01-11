import {createSelector} from '@ngrx/store';
import {Task} from '../../../shared/models/task';
import {taskActions, taskHistoryPageActions} from '../actions';

export interface State {
  entities: {};
  first: number;
  pageSize: number;
  count: number;
}

const initialState: State = {
  first: 0,
  pageSize: 50,
  count: 0,
  entities: {},
};

export function reducer(state = initialState, action: taskActions.Actions | taskHistoryPageActions.Actions): State {
  switch (action.type) {
    case taskHistoryPageActions.INIT_SUCCESS: {
      const {first, pageSize, count, tasks} = action;
      const entities = Task.toEntities(tasks, {});
      return {...state, first, pageSize, count, entities};
    }

    case taskActions.RESTART_SUCCESS:
    case taskActions.DELETE_SUCCESS: {
      const entities = {...state.entities};
      delete entities[action.taskId];
      return {...state, entities};
    }

    default:
      return state;
  }
}

export const getFirst = (state: State) => state.first;
export const getPageSiz = (state: State) => state.pageSize;
export const getCount = (state: State) => state.count;
export const getEntities = (state: State) => state.entities;
export const getTasks = createSelector(getEntities, entities => Object.keys(entities).map(id => entities[id]));
