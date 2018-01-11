import {ChangeDetectionStrategy, Component, Inject, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Actions} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {UtilService} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {examQuestionReviewPageActions} from '../../store';

@Component({
  selector: 'jwjh-exam-question-review-operator-dialog',
  templateUrl: './exam-question-review-operator-dialog.component.html',
  styleUrls: ['./exam-question-review-operator-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ExamQuestionReviewOperatorDialogComponent implements OnDestroy {
  readonly tasks: Task[];
  readonly operators: Operator[];
  private subscriptions = [];

  constructor(private actions$: Actions,
              private store: Store<any>,
              private utilService: UtilService,
              private dialog: MatDialog,
              public dialogRef: MatDialogRef<ExamQuestionReviewOperatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.tasks = data.tasks;
    this.operators = data.operators;
  }

  static open(dialog: MatDialog, data: { tasks: Task[]; operators: Operator[] }) {
    if (data.tasks.length > 0 && data.operators.length > 0) {
      dialog.open(ExamQuestionReviewOperatorDialogComponent, {
        disableClose: true,
        panelClass: 'my-dialog',
        data
      });
    }
  }

  join(operator: Operator) {
    this.store.dispatch(new examQuestionReviewPageActions.Join({
      tasks: this.tasks,
      isManage: true,
      operatorId: operator.id
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
