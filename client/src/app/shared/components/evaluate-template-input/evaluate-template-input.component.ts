import {ChangeDetectionStrategy, Component, forwardRef} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {MatDialog} from '@angular/material';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {UtilService} from '../../../core/services/util.service';
import {EvaluateTemplate} from '../../models/evaluate-template';
import {FeedbackTemplate} from '../../models/feedback-template';
import {EvaluateTemplatePickDialogComponent} from '../evaluate-template-pick-dialog/evaluate-template-pick-dialog.component';
import {EvaluateTemplateUpdateDialogComponent} from '../evaluate-template-update-dialog/evaluate-template-update-dialog.component';

@Component({
  selector: 'jwjh-evaluate-template-input',
  templateUrl: './evaluate-template-input.component.html',
  styleUrls: ['./evaluate-template-input.component.less'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => EvaluateTemplateInputComponent),
    multi: true
  }],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EvaluateTemplateInputComponent implements ControlValueAccessor {
  evaluateTemplate$ = new BehaviorSubject<EvaluateTemplate>(null);
  onModelChange: Function = () => {
  }
  onModelTouched: Function = () => {
  }

  constructor(private dialog: MatDialog,
              private utilService: UtilService) {
  }

  pick() {
    this.dialog.open(EvaluateTemplatePickDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog'
    }).afterClosed()
      .filter(it => it)
      .subscribe(it => this.handleChange(it));
  }

  update() {
    this.dialog.open(EvaluateTemplateUpdateDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {evaluateTemplate: this.evaluateTemplate$.value}
    }).afterClosed()
      .filter(it => it)
      .subscribe(it => this.handleChange(it));
  }

  delete(ev: Event) {
    ev.stopPropagation();
    this.utilService.showConfirm()
      .subscribe(() => this.handleChange(null));
  }

  writeValue(feedbackTemplate: FeedbackTemplate): void {
    this.evaluateTemplate$.next(feedbackTemplate);
  }

  registerOnChange(fn: any): void {
    this.onModelChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onModelTouched = fn;
  }

  private handleChange(feedbackTemplate: FeedbackTemplate) {
    this.evaluateTemplate$.next(feedbackTemplate);
    this.onModelChange(feedbackTemplate);
  }
}
