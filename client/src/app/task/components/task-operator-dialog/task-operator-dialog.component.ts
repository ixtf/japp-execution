import {ChangeDetectionStrategy, Component, Inject, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Actions} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {UtilService} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskService} from '../../services/task.service';
import {taskActions} from '../../store';

@Component({
  selector: 'jwjh-task-operator-dialog',
  templateUrl: './task-operator-dialog.component.html',
  styleUrls: ['./task-operator-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskOperatorDialogComponent implements OnDestroy {
  task$ = new BehaviorSubject<Task>(new Task());
  selectedIndex = 0;
  private subscriptions = [];

  constructor(private actions$: Actions,
              private store: Store<any>,
              private utilService: UtilService,
              private taskService: TaskService,
              public dialogRef: MatDialogRef<TaskOperatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.task$.next(data.task);
    this.selectedIndex = data.selectedIndex || 0;

    this.subscriptions.push(
      actions$.ofType(taskActions.GET_SUCCESS)
        .subscribe((action: any) => {
          if (data.task.id === action.task.id) {
            this.task$.next(Task.assign(action.task));
          }
        })
    );
  }

  static showFollowers(dialog: MatDialog, data: { task: Task }): MatDialogRef<TaskOperatorDialogComponent> {
    return dialog.open(TaskOperatorDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {...data, selectedIndex: 1}
    });
  }

  static showParticipants(dialog: MatDialog, data: { task: Task }): MatDialogRef<TaskOperatorDialogComponent> {
    return dialog.open(TaskOperatorDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  deleteParticipant(operator: Operator) {
    const action = new taskActions.DeleteParticipant(this.task$.value.id, operator.id);
    this.utilService.showConfirm().subscribe(() => this.store.dispatch(action));
  }

  deleteAllParticipant() {
    const action = new taskActions.DeleteAllParticipant(this.task$.value.id);
    this.utilService.showConfirm().subscribe(() => this.store.dispatch(action));
  }

  deleteFollower(operator: Operator) {
    const action = new taskActions.DeleteFollower(this.task$.value.id, operator.id);
    this.utilService.showConfirm().subscribe(() => this.store.dispatch(action));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
