import {ChangeDetectionStrategy, Component, EventEmitter, HostBinding, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/scan';
import {Observable} from 'rxjs/Observable';
import {combineLatest} from 'rxjs/observable/combineLatest';
import {DefaultCompare} from '../../../core/services/util.service';
import {coreIsMobile} from '../../../core/store';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';
import {
  taskProgressPageActions, taskProgressPageTask, taskProgressPageTaskGroup,
  taskProgressPageTaskGroups,
} from '../../store';

@Component({
  moduleId: module.id,
  selector: 'jwjh-task-progress-page',
  templateUrl: './task-progress-page.component.html',
  styleUrls: ['./task-progress-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskProgressPageComponent implements OnDestroy {
  @HostBinding('class.jwjh-page-row') b1 = true;
  @HostBinding('class.jwjh-task-progress-page') b2 = true;
  onTaskGroupFilterNameQ = new EventEmitter<string>();
  taskGroups$: Observable<TaskGroup[]>;
  taskGroup$: Observable<TaskGroup>;
  task$: Observable<Task>;
  taskListFlex$: Observable<number>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private route: ActivatedRoute) {
    this.taskGroups$ = store.select(taskProgressPageTaskGroups);
    this.taskGroup$ = store.select(taskProgressPageTaskGroup);
    this.task$ = store.select(taskProgressPageTask);

    this.taskListFlex$ = combineLatest(this.task$, store.select(coreIsMobile), (task, isMobile) => {
      if (!isMobile) {
        return 40;
      }
      if (task) {
        return 0;
      }
      return 100;
    });

    this.subscriptions.push(
      route.queryParams.scan((acc, cur) => {
        store.dispatch(new taskProgressPageActions.Init(acc, cur));
        return cur;
      }, null).subscribe(),

      this.onTaskGroupFilterNameQ
        .debounceTime(500)
        .distinctUntilChanged()
        .map(q => new taskProgressPageActions.SetTaskGroupFilterNameQ(q))
        .subscribe(it => this.store.dispatch(it))
    );
  }

  sortTasksByModifyDateTimeDesc(): void {
    this.store.dispatch(new taskProgressPageActions.SetTaskSort((o1, o2) => DefaultCompare(o1, o2) * -1));
  }

  sortTasksByTitle(): void {
    this.store.dispatch(new taskProgressPageActions.SetTaskSort((o1, o2) => o1.title.localeCompare(o2.title)));
  }

  filterByAll(): void {
    this.store.dispatch(new taskProgressPageActions.SetTaskSort(null));
    this.store.dispatch(new taskProgressPageActions.SetTaskFilter(null));
  }

  filterByUnfeedbak(): void {
    this.store.dispatch(new taskProgressPageActions.SetTaskSort(null));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
