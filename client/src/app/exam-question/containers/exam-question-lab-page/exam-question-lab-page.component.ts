import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {MatDialog} from '@angular/material';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {ExamQuestion} from '../../../shared/models/exam-question';
import {ExamQuestionLab} from '../../../shared/models/exam-question-lab';
import {ExamQuestionLabOperatorDialogComponent} from '../../components/exam-question-lab-operator-dialog/exam-question-lab-operator-dialog.component';
import {
  examQuestionLabActions, examQuestionLabPageActions, examQuestionLabPageExamQuestions, examQuestionLabPageLab,
  examQuestionLabPageLabs
} from '../../store';

@Component({
  selector: 'jwjh-exam-question-update-page',
  templateUrl: './exam-question-lab-page.component.html',
  styleUrls: ['./exam-question-lab-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ExamQuestionLabPageComponent implements OnDestroy {
  lab$: Observable<ExamQuestionLab>;
  labs$: Observable<ExamQuestionLab[]>;
  examQuestions$: Observable<ExamQuestion[]>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private dialog: MatDialog,
              private route: ActivatedRoute) {
    this.labs$ = this.store.select(examQuestionLabPageLabs);
    this.lab$ = this.store.select(examQuestionLabPageLab);
    this.examQuestions$ = this.store.select(examQuestionLabPageExamQuestions);

    this.subscriptions.push(
      route.queryParams
        .map(it => it.labId)
        .subscribe(labId => {
          store.dispatch(new examQuestionLabPageActions.Init(labId));
        })
    );
  }

  openOperatorDialog() {
    this.lab$.take(1).subscribe(lab => ExamQuestionLabOperatorDialogComponent.open(this.dialog, lab));

  }

  createExamQuestionLab() {
    const name = prompt();
    if (name) {
      const lab = new ExamQuestionLab();
      lab.name = name;
      this.store.dispatch(new examQuestionLabActions.Save(lab));
    }
  }

  updateExamQuestionLab() {
    this.lab$.take(1).subscribe(lab => {
      const name = prompt('', lab.name);
      if (name) {
        lab.name = name;
        this.store.dispatch(new examQuestionLabActions.Save(lab));
      }
    });
  }

  invite() {
    this.lab$.take(1)
      .subscribe(lab => this.store.dispatch(new examQuestionLabPageActions.Invite(lab.id)));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
