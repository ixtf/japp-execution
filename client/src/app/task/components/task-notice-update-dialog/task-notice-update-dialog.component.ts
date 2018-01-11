import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Store} from '@ngrx/store';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {UtilService} from '../../../core/services/util.service';
import {Task} from '../../../shared/models/task';
import {TaskNotice} from '../../../shared/models/task-notice';
import {TaskNoticeService} from '../../services/task-notice.service';
import {taskNoticeActions} from '../../store';

@Component({
  selector: 'jwjh-task-notice-update-dialog',
  templateUrl: './task-notice-update-dialog.component.html',
  styleUrls: ['./task-notice-update-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskNoticeUpdateDialogComponent {
  minDate: Date;
  task: Task;
  taskNotice: TaskNotice;
  taskNotices$ = new BehaviorSubject<TaskNotice[]>([]);
  readonly TIMES = [
    {value: moment([2017, 0, 1, 8, 0, 0, 0]).toDate(), viewValue: '08:00'},
    {value: moment([2017, 0, 1, 9, 0, 0, 0]).toDate(), viewValue: '09:00'},
    {value: moment([2017, 0, 1, 10, 0, 0, 0]).toDate(), viewValue: '10:00'},
    {value: moment([2017, 0, 1, 11, 0, 0, 0]).toDate(), viewValue: '11:00'},
    {value: moment([2017, 0, 1, 12, 0, 0, 0]).toDate(), viewValue: '12:00'},
    {value: moment([2017, 0, 1, 13, 0, 0, 0]).toDate(), viewValue: '13:00'},
    {value: moment([2017, 0, 1, 14, 0, 0, 0]).toDate(), viewValue: '14:00'},
    {value: moment([2017, 0, 1, 15, 0, 0, 0]).toDate(), viewValue: '15:00'},
    {value: moment([2017, 0, 1, 16, 0, 0, 0]).toDate(), viewValue: '16:00'},
    {value: moment([2017, 0, 1, 17, 0, 0, 0]).toDate(), viewValue: '17:00'},
    {value: moment([2017, 0, 1, 18, 0, 0, 0]).toDate(), viewValue: '18:00'},
    {value: moment([2017, 0, 1, 19, 0, 0, 0]).toDate(), viewValue: '19:00'},
    {value: moment([2017, 0, 1, 20, 0, 0, 0]).toDate(), viewValue: '20:00'},
    {value: moment([2017, 0, 1, 21, 0, 0, 0]).toDate(), viewValue: '21:00'},
    {value: moment([2017, 0, 1, 22, 0, 0, 0]).toDate(), viewValue: '22:00'},
    {value: moment([2017, 0, 1, 23, 0, 0, 0]).toDate(), viewValue: '23:00'},
  ];

  constructor(private store: Store<any>,
              private utilService: UtilService,
              private taskNoticeService: TaskNoticeService,
              public dialogRef: MatDialogRef<TaskNoticeUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.minDate = new Date();
    this.task = data.task;
    this.taskNotice = Object.assign(new TaskNotice(), data.taskNotice);
    this.taskNotice.noticeDate = moment(this.taskNotice.noticeDateTime).toDate();
    const time = this.TIMES.find(it => {
      const noticeTime = moment(this.taskNotice.noticeDateTime);
      const value = moment(it.value);
      return noticeTime.hour() === value.hour();
    }) || this.TIMES[0];
    this.taskNotice.noticeTime = time.value;

    taskNoticeService.list(this.task.id)
      .subscribe(it => this.taskNotices$.next(it));
  }

  static open(dialog: MatDialog, data: { task: Task, taskNotice?: TaskNotice }): MatDialogRef<TaskNoticeUpdateDialogComponent> {
    return dialog.open(TaskNoticeUpdateDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  submit() {
    const noticeDate = moment(this.taskNotice.noticeDate);
    const noticeTime = moment(this.taskNotice.noticeTime);
    this.taskNotice.noticeDateTime = moment([
      noticeDate.year(),
      noticeDate.month(),
      noticeDate.date(),
      noticeTime.hour(),
      noticeTime.minute(),
      noticeTime.second(),
      noticeTime.millisecond()
    ]).toDate();
    this.store.dispatch(new taskNoticeActions.Save(this.task.id, this.taskNotice));
    this.dialogRef.close();
  }

  delete(taskNotice: TaskNotice) {
    this.utilService.showConfirm()
      .subscribe(() => {
        this.store.dispatch(new taskNoticeActions.Delete(this.task.id, taskNotice.id));
        const next = this.taskNotices$.value.filter(it => it.id !== taskNotice.id);
        this.taskNotices$.next(next);
      });
  }

  content(taskNotice: TaskNotice) {
    alert(taskNotice.content);
  }

}
