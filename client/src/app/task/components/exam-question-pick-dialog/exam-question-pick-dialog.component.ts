import {ChangeDetectionStrategy, Component, Inject, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef, MatTabChangeEvent} from '@angular/material';
import 'rxjs/add/operator/map';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {deleteEle} from '../../../core/services/util.service';
import {ExamQuestionLabService} from '../../../exam-question/services/exam-question-lab.service';
import {ExamQuestion} from '../../../shared/models/exam-question';
import {ExamQuestionLab} from '../../../shared/models/exam-question-lab';

interface State {
  labs: ExamQuestionLab[];
  examQuestions: ExamQuestion[];
  selecteds: ExamQuestion[];
}

const initialState: State = {
  labs: [],
  examQuestions: [],
  selecteds: [],
};
const getLabs = (state: State) => state.labs;
const getExamQuestions = (state: State) => state.examQuestions.sort((o1, o2) => o1.name.localeCompare(o2.name));
const getSelecteds = (state: State) => state.selecteds;

@Component({
  selector: 'jwjh-exam-question-pick-dialog',
  templateUrl: './exam-question-pick-dialog.component.html',
  styleUrls: ['./exam-question-pick-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ExamQuestionLabService],
})
export class ExamQuestionPickDialogComponent implements OnDestroy {
  state$ = new BehaviorSubject(initialState);
  labs$: Observable<ExamQuestionLab[]>;
  examQuestions$: Observable<ExamQuestion[]>;
  selecteds$: Observable<ExamQuestion[]>;
  private subscriptions = [];

  constructor(private dialog: MatDialog,
              private examQuestionLabService: ExamQuestionLabService,
              private dialogRef: MatDialogRef<ExamQuestionPickDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.labs$ = this.state$.map(getLabs);
    this.examQuestions$ = this.state$.map(getExamQuestions);
    this.selecteds$ = this.state$.map(getSelecteds);

    const selecteds = [...(data.examQuestions || [])];
    const _$: Observable<ExamQuestionLab[]> = this.examQuestionLabService.list();
    _$.subscribe(labs => {
      const next = {...this.state$.value, labs, selecteds};
      this.state$.next(next);
      const labId = labs[0] && labs[0].id;
      this.listExamQuestion(labId);
    });
  }

  selectLab(ev: MatTabChangeEvent) {
    const labId = this.state$.value.labs[ev.index].id;
    this.listExamQuestion(labId);
  }

  clickExamQuestion(examQuestion: ExamQuestion) {
    let selecteds = [...this.state$.value.selecteds];
    const index = selecteds.findIndex(it => it.id === examQuestion.id);
    if (index >= 0) {
      selecteds = deleteEle(selecteds, examQuestion);
    } else {
      selecteds.push(examQuestion);
    }
    const next = {...this.state$.value, selecteds};
    this.state$.next(next);
  }

  isSelected(examQuestion: ExamQuestion): Observable<boolean> {
    return this.selecteds$.map(selecteds => {
      const index = selecteds.findIndex(it => it.id === examQuestion.id);
      return index >= 0;
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

  private listExamQuestion(labId: string) {
    const newVar = labId ? this.examQuestionLabService.listExamQuestion(labId) : of([]);
    newVar.subscribe(examQuestions => {
      const next = {...this.state$.value, examQuestions};
      this.state$.next(next);
    });
  }

}
