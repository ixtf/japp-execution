import {ChangeDetectionStrategy, Component, Inject, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {createSelector, Store} from '@ngrx/store';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/mergeAll';
import 'rxjs/add/operator/reduce';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import {forkJoin} from 'rxjs/observable/forkJoin';
import {of} from 'rxjs/observable/of';
import {Subject} from 'rxjs/Subject';
import {CheckQ, DefaultCompare} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {MyTaskGroup, TaskGroup} from '../../../shared/models/task-group';
import {TaskGroupService} from '../../../task-group/services/task-group.service';
import {taskActions} from '../../store';
import {TaskOperatorDialogComponent} from '../task-operator-dialog/task-operator-dialog.component';

class State {
  taskGroupEntities: { [id: string]: TaskGroup } = {};
  taskGroupId: string;
  taskGroupFilterNameQ: string;
  tasks: Task[] = [];
}

const getTaskGroupEntities = (state: State) => state.taskGroupEntities;
const getTaskGroupFilterNameQ = (state: State) => state.taskGroupFilterNameQ;
const getTaskGroups = createSelector(getTaskGroupEntities, getTaskGroupFilterNameQ, (entities, nameQ) => {
  return Object.keys(entities)
    .map(it => entities[it])
    .filter(it => CheckQ(it.name, nameQ))
    .sort(DefaultCompare);
});
const getTaskGroupId = (state: State) => state.taskGroupId;
const getTaskGroup = createSelector(getTaskGroupEntities, getTaskGroupId, (entities, id) => entities[id]);
const getTasks = (state: State) => state.tasks.sort(DefaultCompare);

@Component({
  selector: 'jwjh-task-operator-import-dialog',
  templateUrl: './task-operator-import-dialog.component.html',
  styleUrls: ['./task-operator-import-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskOperatorImportDialogComponent implements OnDestroy {
  readonly task: Task;
  state$ = new BehaviorSubject(new State());
  taskGroups$: Observable<TaskGroup[]>;
  taskGroup$: Observable<TaskGroup>;
  tasks$: Observable<Task[]>;
  onTaskGroupFilterNameQ = new Subject<string>();
  private subscriptions = [];
  private followers: Operator[] = [];
  private participants: Operator[] = [];

  constructor(private store: Store<any>,
              private dialogRef: MatDialogRef<TaskOperatorImportDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private dialog: MatDialog,
              private taskGroupService: TaskGroupService) {
    this.task = Task.assign(data.task);
    this.taskGroups$ = this.state$.map(getTaskGroups);
    this.taskGroup$ = this.state$.map(getTaskGroup);
    this.tasks$ = this.state$.map(getTasks);
    this.selectTaskGroup(this.task.taskGroup);

    this.subscriptions.push(
      this.onTaskGroupFilterNameQ
        .debounceTime(300)
        .distinctUntilChanged()
        .subscribe(taskGroupFilterNameQ => this.state$.next({...this.state$.value, taskGroupFilterNameQ}))
    );
    dialogRef.beforeClose().subscribe(() => {
      if (this.followers.length > 0) {
        this.store.dispatch(new taskActions.ImportFollowers(this.task.id, this.followers));
      }
      if (this.participants.length > 0) {
        this.store.dispatch(new taskActions.ImportParticipants(this.task.id, this.participants));
      }
    });
  }

  static open(dialog: MatDialog, data: { task: Task }): MatDialogRef<TaskOperatorImportDialogComponent> {
    return dialog.open(TaskOperatorImportDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  selectTaskGroup(taskGroup: TaskGroup) {
    let taskGroupEntities$: Observable<{ [id: string]: TaskGroup }> = of(this.state$.value.taskGroupEntities);
    if (Object.keys(this.state$.value.taskGroupEntities).length === 0) {
      taskGroupEntities$ = this.taskGroupService.list()
        .flatMap(taskGroups => [MyTaskGroup].concat(taskGroups || []))
        .reduce((acc: { [id: string]: TaskGroup }, cur: TaskGroup) => {
          acc[cur.id] = cur;
          return acc;
        }, {});
    }
    const tasks$: Observable<Task[]> = this.taskGroupService.listTask(taskGroup.id);
    forkJoin(taskGroupEntities$, tasks$, (taskGroupEntities, tasks) => {
      tasks = tasks.map(it => Task.assign(it));
      const next = {...this.state$.value, taskGroupEntities, tasks, taskGroupId: taskGroup.id};
      this.state$.next(next);
    }).subscribe();
  }

  showOperators(task: Task) {
    this.dialog.open(TaskOperatorDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {task}
    });
  }

  importOperators(task: Task) {
    this.followers = this.followers.concat(task.followers || []);
    this.participants = this.participants.concat(task.participants || []);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
