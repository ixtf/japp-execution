import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {EvaluateField} from '../../models/evaluate-field';

@Component({
  selector: 'jwjh-evaluate-field-update-dialog',
  templateUrl: './evaluate-field-update-dialog.component.html',
  styleUrls: ['./evaluate-field-update-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EvaluateFieldUpdateDialogComponent {
  title: string;
  field: EvaluateField;

  constructor(private dialogRef: MatDialogRef<EvaluateFieldUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.field = Object.assign(new EvaluateField(), data.field);
    this.title = 'NAV.FIELD-' + (this.field.id ? 'UPDATE' : 'CREATE');
  }

  submit() {
    this.dialogRef.close(this.field);
  }

}
