import {createSelector} from '@ngrx/store';
import {TaskGroup} from '../../../shared/models/task-group';
import {taskGroupUpdatePageActions} from '../actions';

export interface State {
  taskGroup: TaskGroup;
}

const initialState: State = {
  taskGroup: new TaskGroup(),
};

export function reducer(state = initialState, action: taskGroupUpdatePageActions.Actions): State {
  switch (action.type) {
    case taskGroupUpdatePageActions.INIT_SUCCESS: {
      return {
        ...state,
        taskGroup: action.taskGroup,
      };
    }

    default:
      return state;
  }
}

export const getTaskGroup = (state: State) => state.taskGroup;
export const getTitle = createSelector(getTaskGroup, it => 'NAV.TASKGROUP-' + (it.id ? 'UPDATE' : 'CREATE'));
