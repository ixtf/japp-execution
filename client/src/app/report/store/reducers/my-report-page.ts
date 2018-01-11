import {isNumber} from 'util';
import {DefaultCompare} from '../../../core/services/util.service';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';
import {myReportPageActions} from '../actions';

export interface State {
  taskGroups: TaskGroup[];
  tasks: Task[];
  taskStatistics: any;
  taskFilterStatus: string;
  taskFilterRole: string;
  pickedTaskGroupIds?: string[];
}

export const TASKFILTER_ROLES = ['CHARGER', 'PARTICIPANT'];
export const TASKFILTER_STATUSES = ['ALL', 'NOT_STARTED', 'RUNNING'];

const initialState: State = {
  taskGroups: [],
  tasks: [],
  taskStatistics: {},
  taskFilterRole: TASKFILTER_ROLES[0],
  taskFilterStatus: TASKFILTER_STATUSES[0],
};

function statisticTask(task: Task): any {
  const result: any = {feedbacked: true, commented: true};
  if (moment(task.startDate).isAfter(moment())) {
    result.isNotStarted = true;
    return result;
  }
  result.items = task.participants.map(participant => ({participant}));
  if (task.taskFeedbacks) {
    task.taskFeedbacks.forEach(taskFeedback => {
      const item = result.items.find(it => it.participant.id === taskFeedback.creator.id);
      if (item) {
        item.feedbacked = true;
        if (!item.commented) {
          item.commented = taskFeedback.taskFeedbackComments && taskFeedback.taskFeedbackComments.length > 0;
        }
      }
    });
  }
  result.items.forEach(item => {
    if (!item.feedbacked) {
      result.feedbacked = false;
    }
    if (!item.commented) {
      result.commented = false;
    }
  });
  result.isFinished = result.feedbacked && result.commented;
  result.feedbackedCount = result.items.reduce((acc, cur) => acc + cur.feedbacked ? 1 : 0, 0);
  result.commentedCount = result.items.reduce((acc, cur) => acc + cur.commented ? 1 : 0, 0);
  return result;
}

export function reducer(state = initialState, action: myReportPageActions.Actions): State {
  switch (action.type) {
    case myReportPageActions.INIT_SUCCESS: {
      let taskGroups = action.payload.taskGroups || [];
      taskGroups = taskGroups.sort(DefaultCompare);
      let tasks = action.payload.tasks || [];
      tasks = tasks.filter(task => task.participants && task.participants.length > 0);
      const taskStatistics = tasks.reduce((acc, cur) => {
        acc[cur.id] = statisticTask(cur);
        return acc;
      }, {});
      return {
        ...state,
        taskGroups,
        tasks,
        taskStatistics,
        taskFilterStatus: TASKFILTER_STATUSES[0],
      };
    }

    case myReportPageActions.SET_TASKFILTER_STATUS: {
      const taskFilterStatus = isNumber(action.payload) ? TASKFILTER_STATUSES[action.payload] : action.payload;
      return {
        ...state,
        taskFilterStatus: taskFilterStatus || TASKFILTER_STATUSES[0],
      };
    }

    case myReportPageActions.SET_TASKFILTER_ROLE: {
      const taskFilterRole = isNumber(action.payload) ? TASKFILTER_ROLES[action.payload] : action.payload;
      return {
        ...state,
        taskFilterRole: taskFilterRole || TASKFILTER_ROLES[0],
      };
    }

    default:
      return state;
  }
}

export const taskFilterStatus = (state: State) => state.taskFilterStatus;
export const taskFilterRole = (state: State) => state.taskFilterRole;
export const taskGroups = (state: State) => state.taskGroups;
export const pickedTaskGroupIds = (state: State) => state.pickedTaskGroupIds;
export const taskStatistics = (state: State) => state.taskStatistics;
export const tasks = (state: State) => state.tasks;
