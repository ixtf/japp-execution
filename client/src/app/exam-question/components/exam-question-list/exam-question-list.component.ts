import {Component, Input} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Store} from '@ngrx/store';
import {UtilService} from '../../../core/services/util.service';
import {ExamQuestion} from '../../../shared/models/exam-question';
import {FileShowDialogComponent} from '../../../upload/components/file-show-dialog/file-show-dialog.component';
import {examQuestionActions} from '../../store';

@Component({
  selector: 'jwjh-exam-question-list',
  templateUrl: './exam-question-list.component.html',
  styleUrls: ['./exam-question-list.component.less']
})
export class ExamQuestionListComponent {
  @Input()
  examQuestions: ExamQuestion[];
  @Input()
  isShowDelete: boolean;

  constructor(private store: Store<any>,
              private dialog: MatDialog,
              private utilService: UtilService) {
  }

  showQuestion(examQuestion: ExamQuestion) {
    FileShowDialogComponent.open(this.dialog, examQuestion.question, examQuestion.name);
  }

  showAnswer(examQuestion: ExamQuestion) {
    FileShowDialogComponent.open(this.dialog, examQuestion.answer, '');
  }

  delete(examQuestion: ExamQuestion) {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new examQuestionActions.Delete(examQuestion.id)));
  }

}
