import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {Task} from '../../../shared/models/task';
import {TaskEvaluate} from '../../../shared/models/task-evaluate';

@Component({
  selector: 'jwjh-task-evaluate-list',
  templateUrl: './task-evaluate-list.component.html',
  styleUrls: ['./task-evaluate-list.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskEvaluateListComponent implements OnInit {
  @Input()
  task: Task;
  @Input()
  taskEvaluates: TaskEvaluate[];

  constructor() {
  }

  ngOnInit() {
  }

}
