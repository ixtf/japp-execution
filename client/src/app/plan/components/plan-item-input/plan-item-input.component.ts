import {ChangeDetectionStrategy, Component, forwardRef} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {MatDialog} from '@angular/material';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {PlanItem} from '../../../shared/models/plan-item';
import {PlanItemUpdateDialogComponent} from '../plan-item-update-dialog/plan-item-update-dialog.component';

@Component({
  selector: 'jwjh-plan-item-input',
  templateUrl: './plan-item-input.component.html',
  styleUrls: ['./plan-item-input.component.less'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => PlanItemInputComponent),
    multi: true
  }],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlanItemInputComponent implements ControlValueAccessor {
  planItems$ = new BehaviorSubject<PlanItem[]>([]);
  onModelChange: Function = () => {
  }
  onModelTouched: Function = () => {
  }

  constructor(private dialog: MatDialog,
              private utilService: UtilService,
              private apiService: ApiService) {
  }

  update(item?: PlanItem): void {
    PlanItemUpdateDialogComponent.open(this.dialog, {item})
      .afterClosed()
      .filter(it => it)
      .subscribe(it => {
        let next = [...(this.planItems$.value || [])];
        const i = next.indexOf(item);
        if (i < 0) {
          next = [it].concat(next);
        } else {
          next.splice(i, 1);
          next = [it].concat(next);
        }
        this.handleChange(next);
      });
  }

  pick(): void {
  }

  delete(item: PlanItem): void {
    this.utilService.showConfirm().subscribe(() => {
      let next = [...(this.planItems$.value || [])];
      const i = next.indexOf(item);
      if (i < 0) {
        next = next.filter(it => it.id !== item.id);
      } else {
        next.splice(i, 1);
      }
      this.handleChange(next);
    });
  }

  writeValue(obj: PlanItem[]): void {
    this.planItems$.next(obj);
  }

  registerOnChange(fn: any): void {
    this.onModelChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onModelTouched = fn;
  }

  private handleChange(next: PlanItem[]) {
    this.planItems$.next(next || []);
    this.onModelChange(next);
  }

}
