import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Store} from '@ngrx/store';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskEvaluate} from '../../../shared/models/task-evaluate';

@Component({
  selector: 'jwjh-task-evaluate-update-dialog',
  templateUrl: './task-evaluate-update-dialog.component.html',
  styleUrls: ['./task-evaluate-update-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskEvaluateUpdateDialogComponent {
  readonly task: Task;
  readonly taskEvaluate: TaskEvaluate;
  readonly form: FormGroup;

  constructor(private store: Store<any>,
              private fb: FormBuilder,
              private dialogRef: MatDialogRef<TaskEvaluateUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.task = data.task;
    this.taskEvaluate = Object.assign(new TaskEvaluate(), data.taskEvaluate);
    this.form = fb.group({
      'id': this.taskEvaluate.id,
      'executor': data.executor,
      'note': this.taskEvaluate.note,
      'fieldsValue': this.fb.array([]),
    });
    this._setFieldsValue();
  }

  get executor() {
    return this.form.get('executor');
  }

  get note() {
    return this.form.get('note');
  }

  get fieldsValue(): FormArray {
    return this.form.get('fieldsValue') as FormArray;
  }

  static open(dialog: MatDialog, data: { task: Task, executor: Operator, taskEvaluate?: TaskEvaluate }): MatDialogRef<TaskEvaluateUpdateDialogComponent> {
    return dialog.open(TaskEvaluateUpdateDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  getFieldName(id: string): string {
    const find = this.task.evaluateTemplate.fields.find(it => it.id === id);
    return find && find.name;
  }

  submit() {
    this.dialogRef.close(this.form.value);
  }

  private _setFieldsValue() {
    const formGroups = this.task.evaluateTemplate.fields.map(it => this.fb.group({
      'id': it.id,
      'value': [null, [Validators.required, Validators.min(0)]]
    }));
    const formArray = this.fb.array(formGroups);
    this.form.setControl('fieldsValue', formArray);
  }
}
