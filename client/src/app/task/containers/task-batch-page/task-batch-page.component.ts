import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatDialog} from '@angular/material';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/observable/fromEvent';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/take';
import {Observable} from 'rxjs/Observable';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';
import {NicknamesUpdateDialogComponent} from '../../components/nicknames-update-dialog/nicknames-update-dialog.component';
import {
  taskBatchPageActions, taskBatchPageDestTasks, taskBatchPageSourceTasks, taskBatchPageTaskGroup,
  taskBatchPageTaskGroups,
} from '../../store';

@Component({
  selector: 'jwjh-task-batch-page',
  templateUrl: './task-batch-page.component.html',
  styleUrls: ['./task-batch-page.component.less']
})
export class TaskBatchPageComponent implements OnInit, OnDestroy {
  @ViewChild('taskGroupFilterNameQ') taskGroupFilterNameQ: ElementRef;
  taskGroups$: Observable<TaskGroup[]>;
  taskGroup$: Observable<TaskGroup>;
  sourceTasks$: Observable<Task[]>;
  destTasks$: Observable<Task[]>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private router: Router,
              private route: ActivatedRoute,
              private dialog: MatDialog) {
    this.taskGroups$ = store.select(taskBatchPageTaskGroups);
    this.taskGroup$ = store.select(taskBatchPageTaskGroup);
    this.sourceTasks$ = store.select(taskBatchPageSourceTasks);
    this.destTasks$ = store.select(taskBatchPageDestTasks);
  }

  ngOnInit() {
    this.subscriptions.push(
      this.route.queryParams.scan((acc, cur) => {
        this.store.dispatch(new taskBatchPageActions.Init(acc, cur));
        return cur;
      }, null).subscribe(),

      Observable.fromEvent(this.taskGroupFilterNameQ.nativeElement, 'keyup')
        .debounceTime(300)
        .distinctUntilChanged()
        .subscribe((ev: Event) => {
          ev.stopPropagation();
          const q = this.taskGroupFilterNameQ.nativeElement.value;
          this.store.dispatch(new taskBatchPageActions.SetTaskGroupFilterNameQ(q));
        })
    );
  }

  changeTaskGroup(taskGroup: TaskGroup): void {
    this.router.navigate(['tasks', 'batch'], {queryParams: {taskGroupId: taskGroup.id}});
  }

  toDest(task: Task): void {
    this.store.dispatch(new taskBatchPageActions.ToDest(task));
  }

  toDestAll(): void {
    this.store.dispatch(new taskBatchPageActions.ToDestAll());
  }

  toSource(task: Task): void {
    this.store.dispatch(new taskBatchPageActions.ToSource(task));
  }

  toSourceAll(): void {
    this.store.dispatch(new taskBatchPageActions.ToSourceAll());
  }

  tasksInvite(): void {
    this.destTasks$.take(1).subscribe(tasks => {
      this.store.dispatch(new taskBatchPageActions.TasksInvite(tasks));
    });
  }

  tasksFollowInvite(): void {
    this.destTasks$.take(1).subscribe(tasks => {
      this.store.dispatch(new taskBatchPageActions.TasksFollowInvite(tasks));
    });
  }

  updateNickname(): void {
    this.destTasks$.take(1).subscribe(tasks => {
      NicknamesUpdateDialogComponent.open(this.dialog, {tasks});
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
