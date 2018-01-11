import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {TranslateService} from '@ngx-translate/core';
import {downEle, upEle} from '../../../core/services/util.service';
import {Field, FIELD_TYPES} from '../../models/field';

@Component({
  selector: 'jwjh-field-update-dialog',
  templateUrl: './field-update-dialog.component.html',
  styleUrls: ['./field-update-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FieldUpdateDialogComponent {
  types = [...FIELD_TYPES];
  title: string;
  field: Field;

  constructor(private translate: TranslateService,
              private dialogRef: MatDialogRef<FieldUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.field = Object.assign(new Field(), data.field);
    this.title = 'NAV.FIELD-' + (this.field.id ? 'UPDATE' : 'CREATE');
  }

  submit() {
    if (!this.field.name || !this.field.type) {
      return;
    }
    this.dialogRef.close(this.field);
  }

  updateSelectOption(v: string) {
    const title = 'NAV.SELECTOPTION-' + (v ? 'UPDATE' : 'CREATE');
    this.translate.get(title)
      .map(res => prompt(res, v))
      .filter(it => !!it)
      .subscribe(newV => {
        const selectOptions = this.field.selectOptions || [];
        const i = selectOptions.indexOf(v);
        if (i < 0) {
          selectOptions.push(newV);
        } else {
          selectOptions.splice(i, 1, newV);
        }
        this.field.selectOptions = selectOptions;
      });
  }

  deleteSelectOption(v: string) {
    const i = this.field.selectOptions.indexOf(v);
    if (i >= 0) {
      this.field.selectOptions.splice(i, 1);
    }
  }

  upSelectOption(v: string) {
    upEle(this.field.selectOptions, v);
  }

  downSelectOption(v: string) {
    downEle(this.field.selectOptions, v);
  }

}
