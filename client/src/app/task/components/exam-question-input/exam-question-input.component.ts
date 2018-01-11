import {ChangeDetectionStrategy, Component, forwardRef} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {MatDialog} from '@angular/material';
import {Store} from '@ngrx/store';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {UtilService} from '../../../core/services/util.service';
import {ExamQuestion} from '../../../shared/models/exam-question';
import {FileShowDialogComponent} from '../../../upload/components/file-show-dialog/file-show-dialog.component';
import {ExamQuestionPickDialogComponent} from '../exam-question-pick-dialog/exam-question-pick-dialog.component';

@Component({
  selector: 'jwjh-exam-question-input',
  templateUrl: './exam-question-input.component.html',
  styleUrls: ['./exam-question-input.component.less'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => ExamQuestionInputComponent),
    multi: true
  }],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ExamQuestionInputComponent implements ControlValueAccessor {
  examQuestions$ = new BehaviorSubject<ExamQuestion[]>([]);
  onModelChange: Function = () => {
  }
  onModelTouched: Function = () => {
  }

  constructor(private store: Store<any>,
              private dialog: MatDialog,
              private utilService: UtilService) {
  }

  pick() {
    this.dialog.open(ExamQuestionPickDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {examQuestions: this.examQuestions$.value}
    }).afterClosed()
      .filter(it => it)
      .subscribe(it => this.handleChange(it));
  }

  showQuestion(examQuestion: ExamQuestion) {
    FileShowDialogComponent.open(this.dialog, examQuestion.question, '');
  }

  showAnswer(examQuestion: ExamQuestion) {
    FileShowDialogComponent.open(this.dialog, examQuestion.answer, '');
  }

  delete(examQuestion: ExamQuestion) {
    this.utilService.showConfirm()
      .subscribe(() => {
        const examQuestions = this.examQuestions$.value.filter(it => it.id !== examQuestion.id);
        this.handleChange(examQuestions);
      });
  }

  writeValue(examQuestions: ExamQuestion[]): void {
    this.examQuestions$.next(examQuestions);
  }

  registerOnChange(fn: any): void {
    this.onModelChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onModelTouched = fn;
  }

  private handleChange(examQuestions: ExamQuestion[]) {
    this.examQuestions$.next(examQuestions);
    this.onModelChange(examQuestions);
  }

}
