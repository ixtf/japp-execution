import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';

@Component({
  selector: 'jwjh-task-group-list',
  templateUrl: './task-group-list.component.html',
  styleUrls: ['./task-group-list.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskGroupListComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {
  }

}
