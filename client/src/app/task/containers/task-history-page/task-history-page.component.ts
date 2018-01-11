import {CollectionViewer} from '@angular/cdk/collections';
import {DataSource} from '@angular/cdk/table';
import {ChangeDetectionStrategy, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material';
import {Store} from '@ngrx/store';
import {TranslateService} from '@ngx-translate/core';
import {Observable} from 'rxjs/Observable';
import {UtilService} from '../../../core/services/util.service';
import {Task} from '../../../shared/models/task';
import {
  taskActions, taskHistoryPageActions, taskHistoryPageCount, taskHistoryPagePageSiz,
  taskHistoryPageTasks
} from '../../store';

@Component({
  selector: 'jwjh-task-history-page',
  templateUrl: './task-history-page.component.html',
  styleUrls: ['./task-history-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskHistoryPageComponent extends DataSource<Task> implements OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  taskDataSource = this;
  displayedColumns = ['title', 'taskGroup', 'btns'];
  tasks$: Observable<Task[]>;
  count$: Observable<number>;
  pageSize$: Observable<number>;

  constructor(private store: Store<any>,
              private translate: TranslateService,
              private utilService: UtilService) {
    super();
    this.tasks$ = store.select(taskHistoryPageTasks);
    this.count$ = store.select(taskHistoryPageCount);
    this.pageSize$ = store.select(taskHistoryPagePageSiz);
    this.store.dispatch(new taskHistoryPageActions.Init(0, 10));
  }

  ngOnInit(): void {
    // todo  provider MdPaginatorIntl
    this.translate.get('PAGINATOR.itemsPerPageLabel')
      .subscribe(s => this.paginator._intl.itemsPerPageLabel = s);
  }

  restart(task: Task) {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new taskActions.Restart(task.id)));
  }

  delete(task: Task) {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new taskActions.Delete(task.id)));
  }

  connect(collectionViewer: CollectionViewer): Observable<Task[]> {
    return this.tasks$;
  }

  disconnect(collectionViewer: CollectionViewer): void {
  }

  onPage(ev: PageEvent) {
    const pageSize = ev.pageSize;
    const first = ev.pageIndex * pageSize;
    this.store.dispatch(new taskHistoryPageActions.Init(first, pageSize));
  }

}
