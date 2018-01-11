import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {Task} from '../../../shared/models/task';
import {TaskEvaluate} from '../../../shared/models/task-evaluate';
import {taskEvaluateChartOptions} from '../../../shared/util';

@Component({
  selector: 'jwjh-task-evaluate',
  templateUrl: './task-evaluate.component.html',
  styleUrls: ['./task-evaluate.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskEvaluateComponent implements OnInit {
  @Input()
  task: Task;
  @Input()
  taskEvaluate: TaskEvaluate;
  chartOptions: any;

  ngOnInit(): void {
    this.chartOptions = taskEvaluateChartOptions(this.task, this.taskEvaluate);
  }

}
