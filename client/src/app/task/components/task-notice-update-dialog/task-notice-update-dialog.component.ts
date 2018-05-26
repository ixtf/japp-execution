import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/startWith';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {UtilService} from '../../../core/services/util.service';
import {Operator} from '../../../shared/models/operator';
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
  readonly TIMES = ['08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00'];
  readonly form: FormGroup;
  minDate = new Date();
  noticeAllCtrl = new FormControl({value: true});
  task: Task;
  taskNotices$ = new BehaviorSubject<TaskNotice[]>([]);

  constructor(private store: Store<any>,
              private fb: FormBuilder,
              private utilService: UtilService,
              private taskNoticeService: TaskNoticeService,
              public dialogRef: MatDialogRef<TaskNoticeUpdateDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.task = data.task;
    this.form = fb.group({
      id: null,
      noticeDate: [new Date(), Validators.required],
      noticeTime: ['08:00', Validators.required],
      content: '',
      receivers: [null, Validators.required],
    });
    this.noticeAllCtrl.valueChanges
      .startWith(true)
      .subscribe(it => {
        if (it) {
          this.receivers.disable();
        } else {
          this.receivers.enable();
        }
      });

    taskNoticeService.list(this.task.id)
      .subscribe(it => this.taskNotices$.next(it));
  }

  static open(dialog: MatDialog, data: { task: Task, taskNotice?: TaskNotice }): MatDialogRef<TaskNoticeUpdateDialogComponent> {
    return dialog.open(TaskNoticeUpdateDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  submit() {
    const value = {...this.form.value};
    value.noticeTime = moment(value.noticeTime, 'HH:mm').toDate();

    this.taskNoticeService.save(this.task.id, value)
      .subscribe(res => {
        const action = value.id
          ? new taskNoticeActions.UpdateSuccess(this.task.id, res)
          : new taskNoticeActions.CreateSuccess(this.task.id, res);
        this.store.dispatch(action);
      });
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

  showContent(taskNotice: TaskNotice) {
    alert(taskNotice.content);
  }

  compareId(o1: Operator, o2: Operator): boolean {
    return o1 && o2 ? o1.id === o2.id : o1 === o2;
  }

  get noticeDate() {
    return this.form.get('noticeDate');
  }

  get noticeTime() {
    return this.form.get('noticeTime');
  }

  get receivers() {
    return this.form.get('receivers');
  }

  get content() {
    return this.form.get('content');
  }
}
