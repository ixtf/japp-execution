import {ChangeDetectionStrategy, Component, forwardRef} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {MatDialog} from '@angular/material';
import 'rxjs/add/operator/filter';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {deleteEle, downEle, upEle, UtilService} from '../../../core/services/util.service';
import {EvaluateField} from '../../../shared/models/evaluate-field';
import {EvaluateFieldUpdateDialogComponent} from '../evaluate-field-update-dialog/evaluate-field-update-dialog.component';

@Component({
  selector: 'jwjh-evaluate-field-input',
  templateUrl: './evaluate-field-input.component.html',
  styleUrls: ['./evaluate-field-input.component.less'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => EvaluateFieldInputComponent),
    multi: true
  }],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EvaluateFieldInputComponent implements ControlValueAccessor {
  fields$ = new BehaviorSubject<EvaluateField[]>(null);
  onModelChange: Function = () => {
  }
  onModelTouched: Function = () => {
  }

  constructor(private dialog: MatDialog,
              private utilService: UtilService) {
    this.fields$.subscribe(it => this.onModelChange(it));
  }

  update(field: EvaluateField, ev: Event): void {
    this.dialog.open(EvaluateFieldUpdateDialogComponent, {
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

  delete(field: EvaluateField, ev: Event): void {
    this.utilService.showConfirm()
      .subscribe(() => {
        const fields = deleteEle(this.fields$.value, field);
        this.fields$.next(fields);
      });
  }

  up(field: EvaluateField, ev: Event): void {
    const fields = upEle(this.fields$.value, field);
    this.fields$.next(fields);
  }

  down(field: EvaluateField, ev: Event): void {
    const fields = downEle(this.fields$.value, field);
    this.fields$.next(fields);
  }

  writeValue(obj: EvaluateField[]): void {
    this.fields$.next(obj);
  }

  registerOnChange(fn: any): void {
    this.onModelChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onModelTouched = fn;
  }

}
