import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {ApiService} from '../../../core/services/api.service';
import {PlanItem} from '../../../shared/models/plan-item';

@Component({
  selector: 'jwjh-plan-item-update-dialog',
  templateUrl: './plan-item-update-dialog.component.html',
  styleUrls: ['./plan-item-update-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlanItemUpdateDialogComponent {
  form: FormGroup;

  constructor(private dialogRef: MatDialogRef<PlanItemUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { item: PlanItem },
              private fb: FormBuilder,
              private apiService: ApiService) {
    const {item} = data;
    this.form = fb.group({
      'id': item.id,
      'title': [item.title, Validators.required],
      'content': item.content,
      'attachments': item.attachments,
      'feedbackTemplate': item.feedbackTemplate,
      'evaluateTemplate': item.evaluateTemplate,
    });
    this.form.patchValue(item);
  }

  get title() {
    return this.form.get('title');
  }

  get content() {
    return this.form.get('content');
  }

  get attachments() {
    return this.form.get('attachments');
  }

  get feedbackTemplate() {
    return this.form.get('feedbackTemplate');
  }

  get evaluateTemplate() {
    return this.form.get('evaluateTemplate');
  }

  static open(dialog: MatDialog, data?: { item?: PlanItem }): MatDialogRef<PlanItemUpdateDialogComponent> {
    data = data || {};
    data.item = data.item || new PlanItem();
    return dialog.open(PlanItemUpdateDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  submit() {
    this.dialogRef.close(this.form.value);
  }
}
