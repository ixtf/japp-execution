import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {TaskGroup} from '../../../shared/models/task-group';
import {taskGroupActions, taskGroupManagePageActions} from '../actions';

export interface State {
  taskGroupEntities: { [id: string]: TaskGroup };
}

const initialState: State = {
  taskGroupEntities: {},
};

export function reducer(state = initialState, action: taskGroupManagePageActions.Actions | taskGroupActions.Actions): State {
  switch (action.type) {
    case taskGroupManagePageActions.INIT_SUCCESS: {
      const taskGroupEntities = (action.taskGroups || []).reduce((acc, cur) => {
        acc[cur.id] = cur;
        return acc;
      }, {});
      return {
        ...state,
        taskGroupEntities,
      };
    }

    case taskGroupActions.GET_SUCCESS:
    case taskGroupActions.CREATE_SUCCESS:
    case taskGroupActions.UPDATE_SUCCESS: {
      const taskGroupEntities = {...state.taskGroupEntities};
      const taskGroup = Object.assign(new TaskGroup(), taskGroupEntities[action.taskGroup.id], action.taskGroup);
      taskGroupEntities[action.taskGroup.id] = taskGroup;
      return {...state, taskGroupEntities};
    }

    default:
      return state;
  }
}

export const getTaskGroupEntities = (state: State) => state.taskGroupEntities;
export const getTaskGroups = createSelector(getTaskGroupEntities, entities => Object.keys(entities).map(it => entities[it]).sort(DefaultCompare));
