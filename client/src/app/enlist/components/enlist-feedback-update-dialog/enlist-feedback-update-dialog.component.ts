import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Store} from '@ngrx/store';
import {Enlist} from '../../../shared/models/enlist';
import {EnlistFeedback} from '../../../shared/models/enlist-feedback';
import {Field} from '../../../shared/models/field';
import {FieldValue} from '../../../shared/models/field-value';
import {enlistFeedbackActions} from '../../store';

@Component({
  selector: 'jwjh-enlist-feedback-update-dialog',
  templateUrl: './enlist-feedback-update-dialog.component.html',
  styleUrls: ['./enlist-feedback-update-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EnlistFeedbackUpdateDialogComponent {
  enlist: Enlist;
  enlistFeedback: EnlistFeedback;

  constructor(private store: Store<any>,
              private dialogRef: MatDialogRef<EnlistFeedbackUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.enlist = data.enlist;
    this.enlistFeedback = Object.assign(new EnlistFeedback(), data.enlistFeedback);
  }

  static open(dialog: MatDialog, data: { enlist: Enlist, enlistFeedback?: EnlistFeedback }): MatDialogRef<EnlistFeedbackUpdateDialogComponent> {
    return dialog.open(EnlistFeedbackUpdateDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data
    });
  }

  submit() {
    this.store.dispatch(new enlistFeedbackActions.Save(this.enlist.id, this.enlistFeedback));
    this.dialogRef.close();
  }

  getFieldValue(field: Field): any {
    this.enlistFeedback.fieldsValue = this.enlistFeedback.fieldsValue || [];
    let fieldValue = this.enlistFeedback.fieldsValue.find(it => it.id === field.id);
    if (!fieldValue) {
      fieldValue = new FieldValue();
      fieldValue.id = field.id;
      this.enlistFeedback.fieldsValue.push(fieldValue);
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
    const fieldValue = this.enlistFeedback.fieldsValue.find(it => it.id === field.id);
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
