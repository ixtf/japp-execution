import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Actions, Effect} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {baseApiUrl} from '../../../../environments/environment';
import {DefaultCompare} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {MyTaskGroup, TaskGroup} from '../../../shared/models/task-group';
import {TaskBatchService} from '../../../task/services/task-batch.service';
import {FileShowDialogComponent} from '../../../upload/components/file-show-dialog/file-show-dialog.component';
import {examQuestionReviewPageActions} from '../../store';

@Injectable()
export class ExamQuestionReviewPageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(examQuestionReviewPageActions.INIT)
    .exhaustMap((action: examQuestionReviewPageActions.Init) => {
      const {isManage} = action.payload.cur;
      let {taskGroupId} = action.payload.cur;
      return this.listTaskGroup(isManage).switchMap(taskGroups => {
        taskGroups = [MyTaskGroup].concat(taskGroups || []).sort(DefaultCompare);
        taskGroupId = taskGroupId || (taskGroups.length > 1 ? taskGroups[1].id : taskGroups[0].id);
        return this.listTask(isManage, taskGroupId)
          .map(tasks => new examQuestionReviewPageActions.InitSuccess(taskGroups, taskGroupId, tasks, isManage));
      }).catch(error => of(new ShowError(error)));
    });

  @Effect()
  join$: Observable<Action> = this.actions$
    .ofType(examQuestionReviewPageActions.JOIN)
    .exhaustMap((action: examQuestionReviewPageActions.Join) => {
      return this.join(action.payload).switchMap(res => {
        FileShowDialogComponent.openByJoin(this.dialog, res);
        return of();
      }).catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions, private http: HttpClient, private dialog: MatDialog, private taskBatchService: TaskBatchService) {
  }

  private listTaskGroup(isManage: boolean): Observable<TaskGroup[]> {
    const pathSuffix = isManage ? 'manageTaskGroups' : 'participantTaskGroups';
    return this.http.get(`${baseApiUrl}/me/${pathSuffix}`);
  }

  private listTask(isManage: boolean, taskGroupId: string): Observable<Task[]> {
    const pathSuffix = isManage ? 'manageTaskGroups' : 'participantTaskGroups';
    return this.http.get(`${baseApiUrl}/me/${pathSuffix}/${taskGroupId}/tasks`);
  }

  private listOperator(tasks: Task[]): Observable<Operator[]> {
    return this.http.post(`${baseApiUrl}/me/manageTaskGroups/operators`, {tasks});
  }

  private join(data: { isManage: boolean; tasks: Task[]; operatorId?: string }): Observable<any> {
    const {isManage, tasks, operatorId} = data;
    const pathSuffix = isManage ? 'managerJoinExamQuestion' : 'joinExamQuestions';
    return this.http.post(`${baseApiUrl}/taskBatch/${pathSuffix}?operatorId=${operatorId || ''}`, {tasks});
  }

}
