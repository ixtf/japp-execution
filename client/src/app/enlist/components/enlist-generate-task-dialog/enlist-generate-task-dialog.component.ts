import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {DefaultCompare} from '../../../core/services/util.service';
import {Enlist} from '../../../shared/models/enlist';
import {MyTaskGroup, TaskGroup} from '../../../shared/models/task-group';
import {TaskGroupService} from '../../../task-group/services/task-group.service';
import {enlistActions} from '../../store';

@Component({
  selector: 'jwjh-enlist-generate-task-dialog',
  templateUrl: './enlist-generate-task-dialog.component.html',
  styleUrls: ['./enlist-generate-task-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [TaskGroupService]
})
export class EnlistGenerateTaskDialogComponent {
  enlist: Enlist;
  readonly generateTaskForm: FormGroup;
  readonly taskGroups$: Observable<TaskGroup[]>;

  constructor(private store: Store<any>,
              private dialogRef: MatDialogRef<EnlistGenerateTaskDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private fb: FormBuilder,
              private taskGroupService: TaskGroupService) {
    this.enlist = data.enlist;
    this.taskGroups$ = this.taskGroupService.list()
      .map(taskGroups => {
        return [MyTaskGroup].concat(taskGroups).sort(DefaultCompare);
      });
    this.generateTaskForm = fb.group({
      'taskGroup': MyTaskGroup,
      'taskGroupName': '',
    });
  }

  get taskGroup() {
    return this.generateTaskForm.get('taskGroup');
  }

  get taskGroupName() {
    return this.generateTaskForm.get('taskGroupName');
  }

  static open(dialog: MatDialog, data: { enlist: Enlist }): MatDialogRef<EnlistGenerateTaskDialogComponent> {
    return dialog.open(EnlistGenerateTaskDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data
    });
  }

  submit() {
    this.store.dispatch(new enlistActions.GenerateTask({
      enlistId: this.enlist.id,
      taskGroup: this.taskGroup.value,
      taskGroupName: this.taskGroupName.value
    }));
    this.dialogRef.close();
  }

}
