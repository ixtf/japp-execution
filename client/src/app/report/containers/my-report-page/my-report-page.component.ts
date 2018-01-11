import {ChangeDetectionStrategy, Component, EventEmitter, OnDestroy} from '@angular/core';
import {MatTabChangeEvent} from '@angular/material';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/pluck';
import {Observable} from 'rxjs/Observable';
import {combineLatest} from 'rxjs/observable/combineLatest';
import {Subject} from 'rxjs/Subject';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';
import {
  myReportPageActions, myReportPageStatePickedTaskGroups, myReportPageStatePickedTasks,
  myReportPageStateTaskFilterRole, myReportPageStateTaskFilterStatus, myReportPageStateTaskStatistics
} from '../../store';
import {TASKFILTER_ROLES, TASKFILTER_STATUSES} from '../../store/reducers/my-report-page';

@Component({
  selector: 'jwjh-my-report-page',
  templateUrl: './my-report-page.component.html',
  styleUrls: ['./my-report-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MyReportPageComponent implements OnDestroy {
  taskFilterRoles = TASKFILTER_ROLES;
  taskFilterStatuses = TASKFILTER_STATUSES;
  chartOptions$ = new Subject<any>();
  pickedTaskGroups$: Observable<TaskGroup[]>;
  pickedTasks$: Observable<Task[]>;
  taskStatistics$: Observable<any[]>;
  taskFilterRole$: Observable<string>;
  onTaskGroupsChange = new EventEmitter<TaskGroup[]>();
  private subscriptions = [];

  constructor(private store: Store<any>) {
    this.taskStatistics$ = store.select(myReportPageStateTaskStatistics);
    this.pickedTaskGroups$ = store.select(myReportPageStatePickedTaskGroups);
    this.pickedTasks$ = store.select(myReportPageStatePickedTasks);
    this.taskFilterRole$ = store.select(myReportPageStateTaskFilterRole);

    this.subscriptions.push(combineLatest(
      this.pickedTasks$,
      this.taskStatistics$,
      store.select(myReportPageStateTaskFilterStatus),
      (tasks, taskStatistics, filterStatus) => {
        const options = this.calcChartOptions(tasks, taskStatistics, filterStatus);
        this.chartOptions$.next(options);
      }).subscribe()
    );
    store.dispatch(new myReportPageActions.Init());
  }

  calcChartOptions(tasks: Task[], taskStatistics: any[], filterStatus: string): any {
    const finishedSum = tasks.reduce((sum, task) => sum + ((taskStatistics[task.id] || {}).isFinished ? 1 : 0), 0);
    const unstartSum = tasks.reduce((sum, task) => sum + ((taskStatistics[task.id] || {}).isNotStarted ? 1 : 0), 0);
    const partSum = tasks.length - finishedSum - unstartSum;
    const seriesData = [];
    const legendData = [];
    const color = [];
    switch (filterStatus) {
      case this.taskFilterStatuses[1]: {
        seriesData.push({value: unstartSum, name: '未开始'});
        legendData.push('未开始');
        color.push('darkgrey');
        break;
      }
      case this.taskFilterStatuses[2]: {
        seriesData.push({value: partSum, name: '进行中'});
        legendData.push('进行中');
        color.push('lightskyblue');
        break;
      }
      default: {
        seriesData.push({value: unstartSum, name: `未开始`});
        seriesData.push({value: partSum, name: `进行中`});
        seriesData.push({value: finishedSum, name: `已完成`});
        legendData.push('未开始');
        legendData.push('进行中');
        legendData.push('已完成');
        color.push('darkgrey', 'lightskyblue', 'limegreen');
        break;
      }
    }
    return {
      title: {
        x: 'center'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: legendData,
      },
      tooltip: {
        show: true,
        formatter: '{b} : {c} ({d}%)'
      },
      series: [{
        type: 'pie',
        data: seriesData,
      }],
      color,
    };
  }

  getTaskStatistic(task: Task): Observable<any> {
    return this.taskStatistics$.pluck(task.id);
  }

  getTasksByTaskGroup(taskGroup: TaskGroup): Observable<Task[]> {
    return this.pickedTasks$.map(tasks => tasks.filter(it => it.taskGroup.id === taskGroup.id));
  }

  setTaskFilterRole(role: string) {
    this.store.dispatch(new myReportPageActions.SetTaskFilterRole(role));
  }

  setTaskFilterStatus(ev: MatTabChangeEvent) {
    this.store.dispatch(new myReportPageActions.SetTaskFilterStatus(ev.index));
  }

  pickTaskGroups() {
    // this.store.dispatch(new myReportPageActions.OpenPickTaskGroupDialog({onTaskGroupsChange: this.onTaskGroupsChange}));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
