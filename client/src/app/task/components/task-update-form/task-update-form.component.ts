import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {isDate} from 'util';
import {toDate} from '../../../core/services/util.service';
import {Task} from '../../../shared/models/task';

@Component({
  selector: 'jwjh-task-update-form',
  templateUrl: './task-update-form.component.html',
  styleUrls: ['./task-update-form.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskUpdateFormComponent {
  readonly taskForm: FormGroup;
  @Output()
  onSubmit = new EventEmitter<Task>();

  constructor(private fb: FormBuilder) {
    this.taskForm = fb.group({
      'id': null,
      'taskGroup': null,
      'title': ['', Validators.required],
      'content': null,
      'attachments': null,
      'feedbackTemplate': null,
      'evaluateTemplate': null,
      'startDate': [new Date(), Validators.required],
    });
  }

  private _task: Task;

  get task(): Task {
    return this._task;
  }

  @Input()
  set task(value: Task) {
    this._task = Task.assign(value);
    if (!isDate(this._task.startDate)) {
      this._task.startDate = toDate(this._task.startDate);
    }
    this.taskForm.patchValue(this._task);
  }

  get title() {
    return this.taskForm.get('title');
  }

  submit() {
    return this.onSubmit.emit(this.taskForm.value);
  }

}
