import {HttpClient} from '@angular/common/http';
import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatDialog} from '@angular/material';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/observable/fromEvent';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/scan';
import 'rxjs/add/operator/take';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {baseApiUrl} from '../../../../environments/environment';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskGroup} from '../../../shared/models/task-group';
import {ExamQuestionReviewOperatorDialogComponent} from '../../components/exam-question-review-operator-dialog/exam-question-review-operator-dialog.component';
import {
  examQuestionReviewPageActions, examQuestionReviewPageDestTasks, examQuestionReviewPageSourceTasks,
  examQuestionReviewPageTaskGroup, examQuestionReviewPageTaskGroups,
} from '../../store';

@Component({
  selector: 'jwjh-exam-question-review-page',
  templateUrl: './exam-question-review-page.component.html',
  styleUrls: ['./exam-question-review-page.component.less']
})
export class ExamQuestionReviewPageComponent implements OnInit, OnDestroy {
  @ViewChild('taskGroupFilterNameQ') taskGroupFilterNameQ: ElementRef;
  taskGroups$: Observable<TaskGroup[]>;
  taskGroup$: Observable<TaskGroup>;
  sourceTasks$: Observable<Task[]>;
  destTasks$: Observable<Task[]>;
  joinOperators$: Observable<Operator[]>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private http: HttpClient,
              private router: Router,
              private route: ActivatedRoute,
              private dialog: MatDialog) {
    this.taskGroups$ = store.select(examQuestionReviewPageTaskGroups);
    this.taskGroup$ = store.select(examQuestionReviewPageTaskGroup);
    this.sourceTasks$ = store.select(examQuestionReviewPageSourceTasks);
    this.destTasks$ = store.select(examQuestionReviewPageDestTasks);
    // this.joinOperators$ = store.select(examQuestionReviewPageJoinOperators);
    this.joinOperators$ = this.destTasks$.switchMap(tasks => {
      const len = tasks && tasks.length;
      if (!len) {
        return of([]);
      }
      return this.http.post(`${baseApiUrl}/me/manageTaskGroups/operators`, {tasks});
    });
  }

  get isManage(): boolean {
    return this.route.snapshot.url[0].path === 'reviewManage';
  }

  ngOnInit() {
    this.subscriptions.push(
      this.route.queryParams.scan((old, cur) => {
        cur = {...cur, isManage: this.isManage};
        this.store.dispatch(new examQuestionReviewPageActions.Init({old, cur}));
        return cur;
      }, null).subscribe(),

      Observable.fromEvent(this.taskGroupFilterNameQ.nativeElement, 'keyup')
        .debounceTime(300)
        .distinctUntilChanged()
        .subscribe((ev: Event) => {
          ev.stopPropagation();
          const q = this.taskGroupFilterNameQ.nativeElement.value;
          this.store.dispatch(new examQuestionReviewPageActions.SetTaskGroupFilterNameQ(q));
        })
    );
  }

  join(operator: Operator): void {
    this.destTasks$.take(1).subscribe(tasks => {
      this.store.dispatch(new examQuestionReviewPageActions.Join({
        tasks,
        isManage: this.isManage,
        operatorId: operator && operator.id
      }));
    });
  }

  joinManage(): void {
    this.destTasks$.take(1).mergeMap(
      tasks => this.joinOperators$.take(1),
      (tasks, operators) => ExamQuestionReviewOperatorDialogComponent.open(this.dialog, {tasks, operators})
    ).subscribe();
  }

  changeTaskGroup(taskGroup: TaskGroup): void {
    this.router.navigate(['examQuestions', this.route.snapshot.url[0].path], {queryParams: {taskGroupId: taskGroup.id}});
  }

  toDest(task: Task): void {
    this.store.dispatch(new examQuestionReviewPageActions.ToDest(task));
  }

  toDestAll(): void {
    this.store.dispatch(new examQuestionReviewPageActions.ToDestAll());
  }

  toSource(task: Task): void {
    this.store.dispatch(new examQuestionReviewPageActions.ToSource(task));
  }

  toSourceAll(): void {
    this.store.dispatch(new examQuestionReviewPageActions.ToSourceAll());
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
