import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Store} from '@ngrx/store';
import {Field} from '../../../shared/models/field';
import {FieldValue} from '../../../shared/models/field-value';
import {Task} from '../../../shared/models/task';
import {TaskFeedback} from '../../../shared/models/task-feedback';
import {taskFeedbackActions} from '../../store';

@Component({
  selector: 'jwjh-task-feedback-update-dialog',
  templateUrl: './task-feedback-update-dialog.component.html',
  styleUrls: ['./task-feedback-update-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskFeedbackUpdateDialogComponent {
  task: Task;
  taskFeedback: TaskFeedback;

  constructor(private store: Store<any>,
              private dialogRef: MatDialogRef<TaskFeedbackUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.task = data.task;
    this.taskFeedback = Object.assign(new TaskFeedback(), data.taskFeedback);
  }

  static open(dialog: MatDialog, data: { task: Task, taskFeedback?: TaskFeedback }): MatDialogRef<TaskFeedbackUpdateDialogComponent> {
    return dialog.open(TaskFeedbackUpdateDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  submit() {
    this.store.dispatch(new taskFeedbackActions.Save(this.task.id, this.taskFeedback));
    this.dialogRef.close();
  }

  getFieldValue(field: Field): any {
    this.taskFeedback.fieldsValue = this.taskFeedback.fieldsValue || [];
    let fieldValue = this.taskFeedback.fieldsValue.find(it => it.id === field.id);
    if (!fieldValue) {
      fieldValue = new FieldValue();
      fieldValue.id = field.id;
      this.taskFeedback.fieldsValue.push(fieldValue);
    }
    switch (field.type) {
      case 'SELECT_MULTIPLE': {
        fieldValue.valuesString = fieldValue.valuesString || [];
        return fieldValue.valuesString;
      }
      case 'BOOLEAN': {
        fieldValue.valueString = fieldValue.valueString || 'true';
        return fieldValue.valueString;
      }
      case 'NUMBER_DOUBLE':
      case 'NUMBER_LONG': {
        fieldValue.valueString = fieldValue.valueString || '0';
        return fieldValue.valueString;
      }
      case 'SELECT_SINGLE': {
        fieldValue.valueString = fieldValue.valueString || field.selectOptions[0];
        return fieldValue.valueString;
      }
      default:
        return fieldValue.valueString || '';
    }
  }

  setFieldValue(field: Field, v) {
    const fieldValue = this.taskFeedback.fieldsValue.find(it => it.id === field.id);
    switch (field.type) {
      case 'SELECT_MULTIPLE': {
        fieldValue.valuesString = v;
        break;
      }
      default: {
        fieldValue.valueString = v;
        break;
      }
    }
  }

}
