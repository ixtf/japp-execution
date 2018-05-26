import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {ApiService} from '../../../core/services/api.service';
import {Task} from '../../../shared/models/task';
import {TaskComplain} from '../../../shared/models/task-complain';
import {taskComplainHandlePageTask, taskComplainHandlePageTaskComplains} from '../../store';

@Component({
  selector: 'jwjh-task-complain-handle-page',
  templateUrl: './task-complain-handle-page.component.html',
  styleUrls: ['./task-complain-handle-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskComplainHandlePageComponent {
  readonly task$: Observable<Task>;
  readonly taskComplains$: Observable<TaskComplain[]>;

  constructor(private store: Store<any>,
              private router: Router,
              private dialog: MatDialog,
              private apiService: ApiService) {
    this.task$ = this.store.select(taskComplainHandlePageTask);
    this.taskComplains$ = this.store.select(taskComplainHandlePageTaskComplains);
  }
}
