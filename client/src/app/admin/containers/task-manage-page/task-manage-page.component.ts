import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatDialog, PageEvent} from '@angular/material';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {ApiService} from '../../../core/services/api.service';
import {Task} from '../../../shared/models/task';
import {TaskOperatorDialogComponent} from '../../../task/components/task-operator-dialog/task-operator-dialog.component';
import {taskManagePageCount, taskManagePagePageSize, taskManagePageTasks} from '../../store';

@Component({
  selector: 'jwjh-task-manage-page',
  templateUrl: './task-manage-page.component.html',
  styleUrls: ['./task-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskManagePageComponent {
  readonly dataSource: TaskDataSource;
  readonly displayedColumns = ['title'];
  readonly tasks$: Observable<Task[]>;
  readonly count$: Observable<number>;
  readonly pageSize$: Observable<number>;

  constructor(private store: Store<any>,
              private router: Router,
              private dialog: MatDialog,
              private apiService: ApiService) {
    this.dataSource = new TaskDataSource(this.store);
    this.tasks$ = this.store.select(taskManagePageTasks);
    this.count$ = this.store.select(taskManagePageCount);
    this.pageSize$ = this.store.select(taskManagePagePageSize);
  }

  onPage(ev: PageEvent) {
    const pageSize = ev.pageSize;
    const first = ev.pageIndex * pageSize;
    this.router.navigate(['admins', 'tasks'], {queryParams: {first, pageSize}});
  }

  showParticipants(task: Task) {
    TaskOperatorDialogComponent.showParticipants(this.dialog, {task});
  }

  showFollowers(task: Task) {
    TaskOperatorDialogComponent.showFollowers(this.dialog, {task});
  }
}

class TaskDataSource extends DataSource<Task> {
  readonly tasks$: Observable<Task[]>;

  constructor(private store: Store<any>) {
    super();
    this.tasks$ = this.store.select(taskManagePageTasks);
  }

  connect(collectionViewer: CollectionViewer): Observable<Task[]> {
    return this.tasks$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }
}
