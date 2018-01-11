import {createSelector} from '@ngrx/store';
import {isNullOrUndefined} from 'util';
import {CheckQ, DefaultCompare} from '../../../core/services/util.service';
import {Task} from '../../../shared/models/task';
import {TaskEvaluate} from '../../../shared/models/task-evaluate';
import {TaskFeedback} from '../../../shared/models/task-feedback';
import {TaskGroup} from '../../../shared/models/task-group';
import {OperatorNicknamePipe} from '../../../shared/services/operator-nickname.pipe';
import {
  taskActions, taskEvaluateActions, taskFeedbackActions, taskFeedbackCommentActions,
  taskProgressPageActions
} from '../actions';

export interface State {
  taskGroupEntities: { [id: string]: TaskGroup };
  taskGroupId: string;
  taskGroupFilterNameQ: string;
  taskEntities: { [id: string]: Task };
  taskId: string | null;
  taskFilter: (task: Task) => boolean;
  taskSort: (o1: Task, o2: Task) => number;
  feedbackSort: (o1: TaskFeedback, o2: TaskFeedback) => number;
  feedbackFilterNameQ: string;
  feedbackFilterNoComment: boolean;
  evaluateSort: (o1: TaskEvaluate, o2: TaskEvaluate) => number;
  evaluateFilterNameQ: string;
  rightTabIndex: number;
}

const initialState: State = {
  taskGroupEntities: {},
  taskGroupId: null,
  taskGroupFilterNameQ: '',
  taskEntities: {},
  taskId: null,
  taskFilter: null,
  taskSort: DefaultCompare,
  feedbackSort: DefaultCompare,
  feedbackFilterNameQ: '',
  feedbackFilterNoComment: false,
  evaluateSort: DefaultCompare,
  evaluateFilterNameQ: '',
  // 默认选中反馈
  rightTabIndex: 1,
};

export function reducer(state = initialState, action: taskEvaluateActions.Actions | taskFeedbackCommentActions.Actions | taskFeedbackActions.Actions | taskActions.Actions | taskProgressPageActions.Actions): State {
  switch (action.type) {
    case taskProgressPageActions.INIT_SUCCESS: {
      const {taskGroups, taskGroupId, tasks, taskId} = action;
      const taskGroupEntities = TaskGroup.toEntities(taskGroups, state.taskGroupEntities);
      const taskEntities = Task.toEntities(tasks, state.taskEntities);
      return {
        ...state, taskGroupEntities, taskEntities, taskGroupId, taskId,
        feedbackSort: DefaultCompare,
        feedbackFilterNameQ: '',
        feedbackFilterNoComment: false,
        evaluateSort: DefaultCompare,
        evaluateFilterNameQ: '',
        showEvaluateList: false,
        rightTabIndex: 1,
      };
    }

    case taskActions.GET_SUCCESS: {
      const task = Task.assign(state.taskEntities[action.task.id], action.task);
      const taskEntities = {...state.taskEntities};
      taskEntities[task.id] = task;
      return {...state, taskEntities};
    }
    case taskActions.GET_CONTEXT_DATA_SUCCESS: {
      const task = Task.assign(state.taskEntities[action.taskId], {taskContextData: action.taskContextData});
      const taskEntities = {...state.taskEntities};
      taskEntities[task.id] = task;
      return {...state, taskEntities};
    }
    case taskActions.GET_OPERATOR_CONTEXT_DATA_SUCCESS: {
      const task = Task.assign(state.taskEntities[action.taskId], {taskOperatorContextData: action.taskOperatorContextData});
      const taskEntities = {...state.taskEntities};
      taskEntities[task.id] = task;
      return {...state, taskEntities};
    }
    case taskActions.DELETE_SUCCESS:
    case taskActions.QUIT_SUCCESS:
    case taskActions.DONE_SUCCESS: {
      const taskEntities = {...state.taskEntities};
      delete taskEntities[action.taskId];
      return {...state, taskEntities};
    }

    case taskFeedbackActions.LIST_SUCCESS: {
      const taskId = action.taskId;
      const taskFeedbacks = (action.taskFeedbacks || []).sort(DefaultCompare);
      const task = Task.assign({id: taskId}, state.taskEntities[taskId], {taskFeedbacks});
      const taskEntities = {...state.taskEntities};
      taskEntities[taskId] = task;
      return {...state, taskEntities};
    }

    case taskEvaluateActions.LIST_SUCCESS: {
      const taskId = action.taskId;
      const taskEvaluates = (action.taskEvaluates || []).sort(DefaultCompare);
      const task = Task.assign({id: taskId}, state.taskEntities[taskId], {taskEvaluates});
      const taskEntities = {...state.taskEntities};
      taskEntities[taskId] = task;
      return {...state, taskEntities};
    }

    case taskFeedbackCommentActions.LIST_SUCCESS: {
      const {taskId, taskFeedbackId, taskFeedbackComments} = action;
      const task = Task.assign({id: taskId}, state.taskEntities[taskId]);
      task.taskFeedbacks = (task.taskFeedbacks || []).map(taskFeedback => {
        if (taskFeedback.id === taskFeedbackId) {
          return {...taskFeedback, taskFeedbackComments};
        }
        return taskFeedback;
      });
      const taskEntities = {...state.taskEntities};
      taskEntities[task.id] = task;
      return {...state, taskEntities};
    }

    case taskProgressPageActions.SET_TASK_GROUP_FILTER_NAMEQ: {
      return {
        ...state,
        taskGroupFilterNameQ: action.payload
      };
    }

    case taskProgressPageActions.SET_TASK_FILTER: {
      return {
        ...state,
        taskFilter: action.payload,
      };
    }

    case taskProgressPageActions.SET_TASK_SORT: {
      return {
        ...state,
        taskSort: action.payload || DefaultCompare,
      };
    }

    case taskProgressPageActions.SET_FEEDBACK_FILTER_NAMEQ: {
      return {
        ...state,
        feedbackFilterNameQ: action.payload
      };
    }

    case taskProgressPageActions.SET_FEEDBACK_FILTER_NOCOMMENT: {
      return {
        ...state,
        feedbackFilterNoComment: isNullOrUndefined(action.payload) ? !state.feedbackFilterNoComment : action.payload,
      };
    }

    case taskProgressPageActions.SET_EVALUATE_FILTER_NAMEQ: {
      return {
        ...state,
        evaluateFilterNameQ: action.payload
      };
    }

    case taskProgressPageActions.SET_RIGHT_TAB_INDEX: {
      return {
        ...state,
        rightTabIndex: action.payload
      };
    }

    default:
      return state;
  }
}

export const getTaskGroupEntities = (state: State) => state.taskGroupEntities;
export const getTaskEntities = (state: State) => state.taskEntities;
export const getTaskGroupFilterNameQ = (state: State) => state.taskGroupFilterNameQ;
export const getTaskGroupId = (state: State) => state.taskGroupId;
export const getTaskId = (state: State) => state.taskId;
export const getTaskFilter = (state: State) => state.taskFilter;
export const getTaskSort = (state: State) => state.taskSort || DefaultCompare;
export const getFeedbackSort = (state: State) => state.feedbackSort || DefaultCompare;
export const getFeedbackFilterNameQ = (state: State) => state.feedbackFilterNameQ;
export const getFeedbackFilterNoComment = (state: State) => state.feedbackFilterNoComment;
export const getEvaluateSort = (state: State) => state.evaluateSort || DefaultCompare;
export const getEvaluateFilterNameQ = (state: State) => state.evaluateFilterNameQ;
export const getRightTabIndex = (state: State) => state.rightTabIndex;
export const getTaskGroups = createSelector(getTaskGroupEntities, getTaskGroupFilterNameQ, (entities, nameQ) => {
  return Object.keys(entities)
    .map(id => entities[id])
    .filter(it => CheckQ(it.name, nameQ))
    .sort(DefaultCompare);
});
export const getTaskGroup = createSelector(getTaskGroupEntities, getTaskGroupId, (entities, id) => entities[id]);
export const getTasks = createSelector(getTaskEntities, getTaskGroupId, getTaskFilter, getTaskSort, (entities, taskGroupId, filter, sort) => {
  return Object.keys(entities)
    .map(it => entities[it])
    .filter(it => taskGroupId === it.taskGroup.id)
    .filter(it => filter ? filter(it) : true)
    .sort((o1, o2) => sort(o1, o2));
});
export const getTask = createSelector(getTaskEntities, getTaskId, (entities, id) => entities[id]);
const checkNoComment = (noComment, taskFeedback: TaskFeedback) => {
  if (noComment) {
    const taskFeedbackComments = taskFeedback.taskFeedbackComments || [];
    if (taskFeedbackComments.length > 0) {
      return false;
    }
  }
  return true;
};
export const getTaskFeedbacks = createSelector(getTask, getFeedbackSort, getFeedbackFilterNameQ, getFeedbackFilterNoComment, (_task, sort, nameQ, noComment) => {
  return (_task && _task.taskFeedbacks || [])
    .filter(taskFeedback => {
      const nickName = OperatorNicknamePipe.get(taskFeedback.creator, _task);
      return CheckQ(nickName, nameQ) && checkNoComment(noComment, taskFeedback);
    })
    .sort((o1, o2) => sort(o1, o2));
});
export const getShowEvaluateList = (state: State) => state.rightTabIndex === 2;
export const getTaskEvaluates = createSelector(getTask, getShowEvaluateList, getEvaluateSort, getEvaluateFilterNameQ, (_task, isShow, sort, nameQ) => {
  return !isShow ? [] : (_task && _task.taskEvaluates || [])
    .filter(taskEvaluate => {
      const nickName = OperatorNicknamePipe.get(taskEvaluate.executor, _task);
      return CheckQ(nickName, nameQ);
    })
    .sort((o1, o2) => sort(o1, o2));
});
