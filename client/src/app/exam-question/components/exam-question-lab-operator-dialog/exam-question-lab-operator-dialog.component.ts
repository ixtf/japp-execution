import {ChangeDetectionStrategy, Component, Inject, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Actions} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {UtilService} from '../../../core/services/util.service';
import {ExamQuestionLab} from '../../../shared/models/exam-question-lab';
import {Operator} from '../../../shared/models/operator';
import {examQuestionLabActions} from '../../store';

@Component({
  selector: 'jwjh-exam-question-lab-operator-dialog',
  templateUrl: './exam-question-lab-operator-dialog.component.html',
  styleUrls: ['./exam-question-lab-operator-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ExamQuestionLabOperatorDialogComponent implements OnDestroy {
  lab$ = new BehaviorSubject<ExamQuestionLab>(new ExamQuestionLab());
  private subscriptions = [];

  constructor(private actions$: Actions,
              private store: Store<any>,
              private utilService: UtilService,
              private dialogRef: MatDialogRef<ExamQuestionLabOperatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.lab$.next(data.lab);

    this.subscriptions.push(
      actions$.ofType(examQuestionLabActions.DELETE_PARTICIPANT_SUCCESS)
        .subscribe((action: examQuestionLabActions.DeleteParticipantSuccess) => {
          const {labId, participantId} = action;
          if (labId === this.lab$.value.id) {
            const participants = (this.lab$.value.participants || []).filter(it => it.id !== participantId);
            const lab = Object.assign(new ExamQuestionLab(), this.lab$.value, {participants});
            this.lab$.next(lab);
          }
        })
    );
  }

  static open(dialog: MatDialog, lab: ExamQuestionLab, title?: string): MatDialogRef<ExamQuestionLabOperatorDialogComponent> {
    return dialog.open(ExamQuestionLabOperatorDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {lab, title}
    });
  }

  deleteParticipant(operator: Operator) {
    const action = new examQuestionLabActions.DeleteParticipant(this.lab$.value.id, operator.id);
    this.utilService.showConfirm().subscribe(() => this.store.dispatch(action));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
