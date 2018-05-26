import {Component, Inject, NgModule} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {coreAuthOperator} from '../../../core/store';
import {EditorModule} from '../../../editor/editor.module';
import {TaskGroup} from '../../models/task-group';
import {SharedModule} from '../../shared.module';

@Component({
  selector: 'jwjh-task-group-sign-view-dialog',
  templateUrl: './task-group-sign-view-dialog.component.html',
  styleUrls: ['./task-group-sign-view-dialog.component.less']
})
export class TaskGroupSignViewDialogComponent {
  readonly taskGroup: TaskGroup;

  constructor(private store: Store<any>,
              private dialog: MatDialog,
              private router: Router,
              private dialogRef: MatDialogRef<TaskGroupSignViewDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: { taskGroup: TaskGroup }) {
    this.taskGroup = data.taskGroup;
  }

  static open(dialog: MatDialog, data: { taskGroup: TaskGroup }): MatDialogRef<TaskGroupSignViewDialogComponent> {
    return dialog.open(TaskGroupSignViewDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  isManager$(): Observable<boolean> {
    return this.store.select(coreAuthOperator).take(1)
      .map(it => it.id === this.taskGroup.modifier.id);
  }

  edit() {
    this.dialogRef.close();
    this.router.navigate(['taskGroups', 'edit'], {queryParams: {id: this.taskGroup.id}});
  }

}

@NgModule({
  imports: [
    SharedModule,
    EditorModule,
  ],
  declarations: [
    TaskGroupSignViewDialogComponent,
  ],
  entryComponents: [
    TaskGroupSignViewDialogComponent,
  ],
  exports: [
    TaskGroupSignViewDialogComponent,
  ],
})
export class TaskGroupSignViewDialogModule {
}
