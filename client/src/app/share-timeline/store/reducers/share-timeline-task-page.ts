import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskOperatorContextData} from '../../../shared/models/task-operator-context-data';
import {shareTimelineTaskPageActions} from '../actions';

export interface State {
  shareOperator: Operator;
  task: Task;
  taskOperatorContextDatas: TaskOperatorContextData[];
  rightTabIndex: number;
}

const initialState: State = {
  shareOperator: null,
  task: null,
  taskOperatorContextDatas: [],
  rightTabIndex: 1,
};

export function reducer(state = initialState, action: shareTimelineTaskPageActions.Actions): State {
  switch (action.type) {
    case shareTimelineTaskPageActions.INIT_SUCCESS: {
      const {shareOperator, taskFeedbacks, taskEvaluates, taskOperatorContextDatas} = action.payload;
      const task = Task.assign(action.payload.task, {taskFeedbacks}, {taskEvaluates});
      return {...state, shareOperator, task, taskOperatorContextDatas, rightTabIndex: 1};
    }
    case shareTimelineTaskPageActions.SET_RIGHT_TAB_INDEX: {
      return {
        ...state,
        rightTabIndex: action.payload
      };
    }
    default:
      return state;
  }
}

export const getTask = (state: State) => state.task;
export const getShareOperator = (state: State) => state.shareOperator;
export const getTaskOperatorContextDatas = (state: State) => state.taskOperatorContextDatas;
export const getRightTabIndex = (state: State) => state.rightTabIndex;
