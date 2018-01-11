import {ChangeDetectionStrategy, Component, forwardRef} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {MatDialog} from '@angular/material';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {deleteEle, downEle, upEle, UtilService} from '../../../core/services/util.service';
import {Field} from '../../models/field';
import {FieldUpdateDialogComponent} from '../field-update-dialog/field-update-dialog.component';

@Component({
  selector: 'jwjh-field-input',
  templateUrl: './field-input.component.html',
  styleUrls: ['./field-input.component.less'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => FieldInputComponent),
    multi: true
  }],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FieldInputComponent implements ControlValueAccessor {
  fields$ = new BehaviorSubject<Field[]>(null);
  onModelChange: Function = () => {
  }
  onModelTouched: Function = () => {
  }

  constructor(private dialog: MatDialog,
              private utilService: UtilService) {
    this.fields$.subscribe(it => this.onModelChange(it));
  }

  update(field: Field, ev: Event): void {
    this.dialog.open(FieldUpdateDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {field}
    }).afterClosed()
      .filter(it => it)
      .subscribe(it => {
        const fields = [...(this.fields$.value || [])];
        if (field) {
          const i = fields.indexOf(field);
          fields.splice(i, 1, it);
        } else {
          fields.push(it);
        }
        this.fields$.next(fields);
      });
  }

  up(field: Field, ev: Event): void {
    const fields = upEle(this.fields$.value, field);
    this.fields$.next(fields);
  }

  down(field: Field, ev: Event): void {
    const fields = downEle(this.fields$.value, field);
    this.fields$.next(fields);
  }

  delete(field: Field, ev: Event): void {
    this.utilService.showConfirm()
      .subscribe(() => {
        const fields = deleteEle(this.fields$.value, field);
        this.fields$.next(fields);
      });
  }

  writeValue(obj: Field[]): void {
    this.fields$.next(obj);
  }

  registerOnChange(fn: any): void {
    this.onModelChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onModelTouched = fn;
  }
}
