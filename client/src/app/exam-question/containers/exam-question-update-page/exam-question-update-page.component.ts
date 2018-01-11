import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {ExamQuestion} from '../../../shared/models/exam-question';
import {ExamQuestionService} from '../../services/exam-question.service';

@Component({
  selector: 'jwjh-exam-question-update-page',
  templateUrl: './exam-question-update-page.component.html',
  styleUrls: ['./exam-question-update-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ExamQuestionUpdatePageComponent implements OnDestroy {
  examQuestion$: Observable<ExamQuestion>;
  private subscriptions = [];
  private labId: string;

  constructor(private store: Store<any>,
              private router: Router,
              private route: ActivatedRoute,
              private examQuestionService: ExamQuestionService) {
    this.examQuestion$ = route.queryParams.switchMap((queryParams) => {
      this.labId = queryParams.labId;
      if (queryParams.examQuestionId) {
        return examQuestionService.get(queryParams.examQuestionId);
      }
      return of(new ExamQuestion());
    });
  }

  save(examQuestion: ExamQuestion) {
    const labId = this.labId || (examQuestion.lab && examQuestion.lab.id);
    this.examQuestionService.save(labId, examQuestion).subscribe(res => {
      this.router.navigate(['/examQuestions/labs'], {
        queryParams: {
          labId: res.lab.id
        }
      });
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
