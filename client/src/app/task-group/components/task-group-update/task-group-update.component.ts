import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {TaskGroup} from '../../../shared/models/task-group';

@Component({
  selector: 'jwjh-task-group-update',
  templateUrl: './task-group-update.component.html',
  styleUrls: ['./task-group-update.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskGroupUpdateComponent {
  btnTxtLogo = 'BUTTON.TASKGROUP-LOGO';
  btnTxtSign = 'BUTTON.TASKGROUP-SIGN';
  @Output()
  onSubmit = new EventEmitter<TaskGroup>();

  private _taskGroup: TaskGroup;

  get taskGroup(): any {
    return this._taskGroup;
  }

  @Input()
  set taskGroup(value: any) {
    this._taskGroup = Object.assign(new TaskGroup(), value);
  }

}
