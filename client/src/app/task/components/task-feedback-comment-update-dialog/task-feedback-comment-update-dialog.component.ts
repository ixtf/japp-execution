import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Store} from '@ngrx/store';
import {Task} from '../../../shared/models/task';
import {TaskFeedback} from '../../../shared/models/task-feedback';
import {TaskFeedbackComment} from '../../../shared/models/task-feedback-comment';
import {taskFeedbackCommentActions} from '../../store';

@Component({
  selector: 'jwjh-task-feedback-comment-update-dialog',
  templateUrl: './task-feedback-comment-update-dialog.component.html',
  styleUrls: ['./task-feedback-comment-update-dialog.component.less']
})
export class TaskFeedbackCommentUpdateDialogComponent {
  readonly task: Task;
  readonly taskFeedback: TaskFeedback;
  readonly taskFeedbackComment: TaskFeedbackComment;

  constructor(private store: Store<any>,
              private dialogRef: MatDialogRef<TaskFeedbackCommentUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.task = data.task;
    this.taskFeedback = data.taskFeedback;
    this.taskFeedbackComment = Object.assign(new TaskFeedbackComment(), data.taskFeedbackComment);
  }

  static open(dialog: MatDialog, data: { task: Task, taskFeedback: TaskFeedback, taskFeedbackComment?: TaskFeedbackComment }): MatDialogRef<TaskFeedbackCommentUpdateDialogComponent> {
    return dialog.open(TaskFeedbackCommentUpdateDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  submit() {
    this.store.dispatch(new taskFeedbackCommentActions.Save(this.task.id, this.taskFeedback.id, this.taskFeedbackComment));
    this.dialogRef.close();
  }

}
