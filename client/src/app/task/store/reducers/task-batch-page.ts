import {createSelector} from '@ngrx/store';
import 'rxjs/add/observable/from';
import {CheckQ, DefaultCompare} from '../../../core/services/util.service';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';
import {taskActions, taskBatchPageActions} from '../actions';

export interface State {
  taskGroupEntities: { [id: string]: TaskGroup };
  taskGroupId: string;
  taskGroupFilterNameQ: string;
  sourceTasks: Task[];
  desTasks: Task[];
}

const initialState: State = {
  taskGroupEntities: {},
  taskGroupId: null,
  taskGroupFilterNameQ: '',
  sourceTasks: [],
  desTasks: [],
};

export function reducer(state = initialState, action: taskActions.Actions | taskBatchPageActions.Actions): State {
  switch (action.type) {
    case taskBatchPageActions.INIT_SUCCESS: {
      const {taskGroups, taskGroupId, tasks} = action;
      const taskGroupEntities = TaskGroup.toEntities(taskGroups, state.taskGroupEntities);
      const sourceTasks = (tasks || []).map(it => Task.assign(it)).sort(DefaultCompare);
      return {...state, taskGroupEntities, taskGroupId, sourceTasks};
    }

    case taskBatchPageActions.SET_TASK_GROUP_FILTER_NAMEQ: {
      return {
        ...state,
        taskGroupFilterNameQ: action.payload
      };
    }

    case taskBatchPageActions.TO_DEST: {
      const {task} = action;
      const desTasks = [task].concat(state.desTasks);
      const sourceTasks = state.sourceTasks.filter(it => it.id !== task.id);
      return {...state, sourceTasks, desTasks};
    }

    case taskBatchPageActions.TO_DEST_ALL: {
      const desTasks = state.sourceTasks.concat(state.desTasks);
      return {...state, desTasks, sourceTasks: []};
    }

    case taskBatchPageActions.TO_SOURCE: {
      const {task} = action;
      const sourceTasks = state.sourceTasks.concat(task);
      const desTasks = state.desTasks.filter(it => it.id !== task.id);
      return {...state, sourceTasks, desTasks};
    }

    case taskBatchPageActions.TO_SOURCE_ALL: {
      const sourceTasks = state.sourceTasks.concat(state.desTasks);
      return {...state, sourceTasks, desTasks: []};
    }

    default:
      return state;
  }
}

export const getTaskGroupEntities = (state: State) => state.taskGroupEntities;
export const getTaskGroupId = (state: State) => state.taskGroupId;
export const getTaskGroupFilterNameQ = (state: State) => state.taskGroupFilterNameQ;
export const getDestTasks = (state: State) => state.desTasks;
export const getSourceTasks = (state: State) => state.sourceTasks;
export const getTaskGroup = createSelector(getTaskGroupEntities, getTaskGroupId, (entities, id) => entities[id]);
export const getTaskGroups = createSelector(getTaskGroupEntities, getTaskGroupFilterNameQ,
  (entities, nameQ) => {
    const res = [];
    Object.keys(entities).forEach(it => {
      const taskGroup = entities[it];
      if (CheckQ(taskGroup.name, nameQ)) {
        res.push(taskGroup);
      }
    });
    return res.sort((o1, o2) => DefaultCompare(o1, o2));
  }
);
