import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Actions} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import {UtilService} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
import {Task} from '../../../shared/models/task';
import {TaskFeedbackService} from '../../services/task-feedback.service';
import {TaskService} from '../../services/task.service';

@Component({
  selector: 'jwjh-task-unfeedbacker-dialog',
  templateUrl: './task-unfeedbacker-dialog.component.html',
  styleUrls: ['./task-unfeedbacker-dialog.component.less']
})
export class TaskUnfeedbackerDialogComponent {
  task$ = new BehaviorSubject<Task>(new Task());
  unfeedbackers$: Observable<Operator[]>;

  constructor(private actions$: Actions,
              private store: Store<any>,
              private utilService: UtilService,
              private taskFeedbackService: TaskFeedbackService,
              private taskService: TaskService,
              public dialogRef: MatDialogRef<TaskUnfeedbackerDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.task$.next(data.task);
    this.unfeedbackers$ = taskFeedbackService.list(data.task.id)
      .map(taskFeedbacks => {
        const feedbackerIds = taskFeedbacks.map(it => it.creator.id);
        return data.task.participants.filter(it => feedbackerIds.indexOf(it.id) < 0);
      });
  }

}
