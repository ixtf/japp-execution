import {createFeatureSelector, createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../core/services/util.service';
import {coreAuthOperator} from '../../core/store/index';
import {isManager, isParticipant} from '../../task/services/task.service';
import {fromMyReportPage} from './reducers';

export * from './actions';

export const featureName = 'report';

export interface State {
  report: ReportState;
}

export interface ReportState {
  myReportPage: fromMyReportPage.State;
}

export const reducers = {
  myReportPage: fromMyReportPage.reducer,
};

export const reportState = createFeatureSelector<ReportState>(featureName);
export const myReportPageState = createSelector(reportState, state => state.myReportPage);

export const myReportPageStateTaskFilterStatus = createSelector(myReportPageState, fromMyReportPage.taskFilterStatus);
export const myReportPageStateTaskFilterRole = createSelector(myReportPageState, fromMyReportPage.taskFilterRole);
export const myReportPageStatePickedTaskGroupIds = createSelector(myReportPageState, fromMyReportPage.pickedTaskGroupIds);
export const myReportPageStateTaskGroups = createSelector(myReportPageState, fromMyReportPage.taskGroups);
export const myReportPageStateTasks = createSelector(myReportPageState, fromMyReportPage.tasks);
export const myReportPageStateTaskStatistics = createSelector(myReportPageState, fromMyReportPage.taskStatistics);

export const myReportPageStatePickedTasks = createSelector(
  myReportPageStateTasks,
  myReportPageStateTaskStatistics,
  myReportPageStateTaskFilterStatus,
  coreAuthOperator,
  myReportPageStateTaskFilterRole,
  myReportPageStatePickedTaskGroupIds,
  (tasks, taskStatistics, status, operator, role, taskGroupIds) => {
    const checkStatus = task => {
      const taskStatistic = taskStatistics[task.id];
      switch (status) {
        case 'RUNNING':
          return !taskStatistic.isNotStarted && !taskStatistic.isFinished;
        case 'NOT_STARTED':
          return taskStatistic.isNotStarted;
        default:
          return true;
      }
    };
    const checkRole = task => (role === 'PARTICIPANT') ? isParticipant(task, operator) : isManager(task, operator);
    const checkTaskGroupIds = task => {
      if (taskGroupIds && taskGroupIds.length > 0) {
        return taskGroupIds.find(it => it === task.taskGroup.id);
      }
      return true;
    };
    return tasks.filter(task => {
      return checkRole(task) && checkStatus(task) && checkTaskGroupIds(task);
    }).sort((o1, o2) => {
      const taskStatistic1 = taskStatistics[o1.id];
      const taskStatistic2 = taskStatistics[o2.id];
      const started1 = !taskStatistic1.isNotStarted;
      const started2 = !taskStatistic2.isNotStarted;
      if (!started1 && started2) {
        return -1;
      }
      if (started1 && !started2) {
        return 1;
      }
      const finished1 = !taskStatistic1.isFinished;
      const finished2 = !taskStatistic2.isFinished;
      if (finished1 && !finished2) {
        return -1;
      }
      if (!finished1 && finished2) {
        return 1;
      }
      return DefaultCompare(o1, o2);
    });
  }
);
export const myReportPageStatePickedTaskGroups = createSelector(
  myReportPageStatePickedTasks,
  tasks => {
    const entities = tasks.map(task => task.taskGroup)
      .reduce((acc, cur) => Object.assign(acc, {[cur.id]: cur}), {});
    return Object.keys(entities).map(id => entities[id]).sort(DefaultCompare);
  }
);
