import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';
import {TaskOperatorContextData} from '../../../shared/models/task-operator-context-data';
import {shareTimelineTaskGroupPageActions} from '../actions';

export interface State {
  shareOperator: Operator;
  taskGroup: TaskGroup;
  taskEntities: { [id: string]: Task };
  taskOperatorContextDataEntities: { [taskId: string]: { [operatorId: string]: TaskOperatorContextData } };
}

const initialState: State = {
  shareOperator: null,
  taskGroup: null,
  taskEntities: {},
  taskOperatorContextDataEntities: {},
};

export function reducer(state = initialState, action: shareTimelineTaskGroupPageActions.Actions): State {
  switch (action.type) {
    case shareTimelineTaskGroupPageActions.INIT_SUCCESS: {
      const {shareOperator, taskObjects, taskGroup} = action.payload;
      const tasks = taskObjects.map(it => {
        const {task, taskFeedbacks, taskEvaluates} = it;
        return Task.assign(task, {taskFeedbacks, taskEvaluates});
      });
      const taskEntities = Task.toEntities(tasks);
      const taskOperatorContextDataEntities = taskObjects.reduce((acc, cur) => {
        const taskId = cur.task.id;
        acc [taskId] = cur.taskOperatorContextDatas.reduce((acc2, cur2) => {
          const operatorId = cur2.operator.id;
          acc2[operatorId] = cur2;
          return acc2;
        }, {});
        return acc;
      }, {});
      return {...state, shareOperator, taskEntities, taskGroup, taskOperatorContextDataEntities};
    }
    default:
      return state;
  }
}

export const getShareOperator = (state: State) => state.shareOperator;
export const getTaskGroup = (state: State) => state.taskGroup;
export const getTaskEntities = (state: State) => state.taskEntities;
export const getTaskOperatorContextDataEntities = (state: State) => state.taskOperatorContextDataEntities;
export const getTasks = createSelector(getTaskEntities, entities => {
  return Object.keys(entities)
    .map(it => entities[it])
    .sort(DefaultCompare);
});
export const getFinishedTaskEntities = createSelector(getShareOperator, getTasks, (shareOperator, tasks) => {
  return tasks.reduce((acc, cur) => {
    let feedbacked = false;
    let feedbackCommented = false;
    const taskFeedback = cur.taskFeedbacks.find(it => it.creator.id === shareOperator.id);
    if (taskFeedback) {
      feedbacked = true;
      const taskFeedbackComments = taskFeedback.taskFeedbackComments || [];
      if (taskFeedbackComments.length > 0) {
        feedbackCommented = true;
      }
    }
    if (feedbacked && feedbackCommented) {
      acc[cur.id] = cur;
    }
    return acc;
  }, {});
});
export const getUnStartedTaskEntities = createSelector(getShareOperator, getTasks, getFinishedTaskEntities, (shareOperator, tasks, finishedTaskEntities) => {
  return tasks.filter(it => !finishedTaskEntities[it.id]).reduce((acc, cur) => {
    const taskFeedback = cur.taskFeedbacks.find(it => it.creator.id === shareOperator.id);
    if (!taskFeedback) {
      acc[cur.id] = cur;
    }
    return acc;
  }, {});
});
export const getChartOptions = createSelector(getShareOperator, getTasks, getFinishedTaskEntities, getUnStartedTaskEntities, getTaskOperatorContextDataEntities,
  (shareOperator, tasks, finishedTaskEntities, unStartedTaskEntities, taskOperatorContextDataEntities) => {
    const isFinished = (task) => {
      return finishedTaskEntities[task.id];
    };
    const isUnStarted = (task) => {
      return unStartedTaskEntities[task.id];
    };
    const seriesData = [];
    const taskSum = tasks.length;
    const finishedSum = tasks.reduce((sum, task) => sum + (isFinished(task) ? 1 : 0), 0);
    const unstartSum = tasks.reduce((sum, task) => sum + (isUnStarted(task) ? 1 : 0), 0);
    const partSum = taskSum - finishedSum - unstartSum;
    seriesData.push({value: unstartSum, name: `未开始`});
    seriesData.push({value: partSum, name: `进行中`});
    seriesData.push({value: finishedSum, name: `已回复确认`});
    return {
      title: {
        x: 'center'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        // data: ['已完成', '已反馈', '已回复', '未开始']
        data: ['未开始', '进行中', '已回复确认']
      },
      tooltip: {
        show: true,
        formatter: '{b} : {c} ({d}%)'
      },
      series: [{
        type: 'pie',
        data: seriesData
      }],
      color: ['darkgrey', 'lightskyblue', 'limegreen']
    };
  });
