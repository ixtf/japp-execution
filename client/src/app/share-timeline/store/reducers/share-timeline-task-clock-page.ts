import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {taskEvaluateChartOptions} from '../../../shared/util';
import {shareTimelineTaskClockPageActions} from '../actions';

export interface State {
  shareOperator: Operator;
  taskId: string;
  taskEntities: { [id: string]: Task };
}

const initialState: State = {
  shareOperator: null,
  taskId: null,
  taskEntities: {},
};

export function reducer(state = initialState, action: shareTimelineTaskClockPageActions.Actions): State {
  switch (action.type) {
    case shareTimelineTaskClockPageActions.INIT_SUCCESS: {
      const {shareOperator, taskId, tasks} = action.payload;
      const taskEntities = Task.toEntities(tasks);
      return {...state, shareOperator, taskId, taskEntities};
    }
    default:
      return state;
  }
}

export const getShareOperator = (state: State) => state.shareOperator;
export const getTaskId = (state: State) => state.taskId;
export const getTaskEntities = (state: State) => state.taskEntities;
export const getTask = createSelector(getTaskEntities, getTaskId, (entities, id) => entities[id]);
export const getTaskGroup = createSelector(getTask, it => it && it.taskGroup);
export const getNickName = createSelector(getShareOperator, getTask, (operator, task) => {
  if (!operator || !task) {
    return null;
  }
  const find = (task.taskOperatorContextDatas || []).find(it => it.operator.id === operator.id);
  return find && find.nickName || operator.nickName || operator.name;
});
export const getTasks = createSelector(getTaskEntities, entities => {
  return Object.keys(entities)
    .map(it => entities[it])
    .sort(DefaultCompare);
});
export const getDays = createSelector(getShareOperator, getTasks, (operator, tasks) => {
  return tasks.reduce((acc, cur) => {
    const find = (cur.taskFeedbacks || []).find(it => {
      return it.creator.id === operator.id && it.taskFeedbackComments && it.taskFeedbackComments.length > 0;
    });
    return find ? ++acc : acc;
  }, 0);
});
export const getPercent = createSelector(getShareOperator, getTasks, (operator, tasks) => {
  const totalCount = tasks && tasks.length;
  if (!totalCount) {
    return 0;
  }
  const count = tasks.reduce((acc, cur) => {
    const find = (cur.taskFeedbacks || []).find(it => {
      return it.creator.id === operator.id && it.taskFeedbackComments && it.taskFeedbackComments.length > 0;
    });
    return find ? ++acc : acc;
  }, 0);
  return count / totalCount;
});
export const getChartOptions = createSelector(getShareOperator, getTask, (operator, task) => {
  if (!(operator && task && task.taskEvaluates)) {
    return null;
  }
  const taskEvaluate = task.taskEvaluates.find(it => it.executor.id === operator.id);
  const options = taskEvaluateChartOptions(task, taskEvaluate);
  return Object.assign(options, {title: {text: taskEvaluate.creator.name + ' 的评价'}});
});
