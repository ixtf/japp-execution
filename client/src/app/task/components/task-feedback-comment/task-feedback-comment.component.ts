import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {isUndefined} from 'util';
import {UtilService} from '../../../core/services/util.service';
import {coreAuthOperator} from '../../../core/store/index';
import {ExamQuestion} from '../../../shared/models/exam-question';
import {Task} from '../../../shared/models/task';
import {TaskFeedback} from '../../../shared/models/task-feedback';
import {TaskFeedbackComment} from '../../../shared/models/task-feedback-comment';
import {FileShowDialogComponent} from '../../../upload/components/file-show-dialog/file-show-dialog.component';
import {taskFeedbackCommentActions} from '../../store';

@Component({
  selector: 'jwjh-task-feedback-comment',
  templateUrl: './task-feedback-comment.component.html',
  styleUrls: ['./task-feedback-comment.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskFeedbackCommentComponent {
  @Input()
  task: Task;
  @Input()
  taskFeedback: TaskFeedback;
  @Input()
  taskFeedbackComment: TaskFeedbackComment;
  @Input()
  private showOnDelete: boolean;

  constructor(private store: Store<any>,
              private dialog: MatDialog,
              private utilService: UtilService) {
  }

  get showDelete$(): Observable<boolean> {
    if (isUndefined(this.showOnDelete)) {
      return this.store.select(coreAuthOperator)
        .take(1)
        .map(operator => this.taskFeedbackComment.creator.id === operator.id);
    }
    return of(this.showOnDelete);
  }

  delete() {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new taskFeedbackCommentActions.Delete(this.task.id, this.taskFeedback.id, this.taskFeedbackComment.id)));
  }

  showQuestion(examQuestion: ExamQuestion) {
    FileShowDialogComponent.open(this.dialog, examQuestion.question, '');
  }

  showAnswer(examQuestion: ExamQuestion) {
    FileShowDialogComponent.open(this.dialog, examQuestion.answer, '');
  }

}
