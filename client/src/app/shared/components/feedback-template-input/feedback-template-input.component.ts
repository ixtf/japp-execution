import {ChangeDetectionStrategy, Component, forwardRef} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {MatDialog} from '@angular/material';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {UtilService} from '../../../core/services/util.service';
import {FeedbackTemplate} from '../../../shared/models/feedback-template';
import {FeedbackTemplatePickDialogComponent} from '../feedback-template-pick-dialog/feedback-template-pick-dialog.component';
import {FeedbackTemplateUpdateDialogComponent} from '../feedback-template-update-dialog/feedback-template-update-dialog.component';

@Component({
  selector: 'jwjh-feedback-template-input',
  templateUrl: './feedback-template-input.component.html',
  styleUrls: ['./feedback-template-input.component.less'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => FeedbackTemplateInputComponent),
    multi: true
  }],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FeedbackTemplateInputComponent implements ControlValueAccessor {
  feedbackTemplate$ = new BehaviorSubject<FeedbackTemplate>(null);
  onModelChange: Function = () => {
  }
  onModelTouched: Function = () => {
  }

  constructor(private dialog: MatDialog,
              private utilService: UtilService) {
  }

  pick(ev: Event) {
    ev.stopPropagation();
    this.dialog.open(FeedbackTemplatePickDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog'
    }).afterClosed()
      .filter(it => it)
      .subscribe(it => this.handleChange(it));
  }

  update(ev: Event) {
    ev.stopPropagation();
    this.dialog.open(FeedbackTemplateUpdateDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {feedbackTemplate: this.feedbackTemplate$.value}
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
    this.feedbackTemplate$.next(feedbackTemplate);
  }

  registerOnChange(fn: any): void {
    this.onModelChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onModelTouched = fn;
  }

  private handleChange(feedbackTemplate: FeedbackTemplate) {
    this.feedbackTemplate$.next(feedbackTemplate);
    this.onModelChange(feedbackTemplate);
  }
}
