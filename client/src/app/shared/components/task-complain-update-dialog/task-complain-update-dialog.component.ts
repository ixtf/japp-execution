import {ChangeDetectionStrategy, Component, Inject, NgModule} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {EditorModule} from '../../../editor/editor.module';
import {Task} from '../../models/task';
import {TaskComplain} from '../../models/task-complain';
import {SharedModule} from '../../shared.module';

@Component({
  selector: 'jwjh-task-complain-update-dialog',
  templateUrl: './task-complain-update-dialog.component.html',
  styleUrls: ['./task-complain-update-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskComplainUpdateDialogComponent {
  readonly task: Task;
  readonly form: FormGroup;

  constructor(private fb: FormBuilder,
              private apiService: ApiService,
              private utilService: UtilService,
              private dialogRef: MatDialogRef<TaskComplainUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: { task: Task, taskComplain: TaskComplain }) {
    this.task = data.task;
    this.form = fb.group({
      'id': null,
      'content': ['', Validators.required],
    });
    this.form.patchValue(data.taskComplain);
  }

  static create(dialog: MatDialog, data: { task: Task }): MatDialogRef<TaskComplainUpdateDialogComponent> {
    const taskComplain = new TaskComplain();
    data = Object.assign(data, {taskComplain});
    return dialog.open(TaskComplainUpdateDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  static update(dialog: MatDialog, data: { taskComplain: TaskComplain }): MatDialogRef<TaskComplainUpdateDialogComponent> {
    const {task} = data.taskComplain;
    data = Object.assign(data, {task});
    return dialog.open(TaskComplainUpdateDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  submit() {
    this.apiService.createTaskComplain(this.task.id, this.form.value)
      .subscribe(() => {
        this.utilService.showSuccess();
        this.dialogRef.close();
      });
  }

  get content() {
    return this.form.get('content');
  }

}

@NgModule({
  declarations: [
    TaskComplainUpdateDialogComponent,
  ],
  entryComponents: [
    TaskComplainUpdateDialogComponent,
  ],
  imports: [
    SharedModule,
    EditorModule,
  ],
  exports: [
    TaskComplainUpdateDialogComponent,
  ],
})
export class TaskComplainUpdateDialogModule {
}
