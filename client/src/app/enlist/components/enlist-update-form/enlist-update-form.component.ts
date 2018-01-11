import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {isDate} from 'util';
import {toDate} from '../../../core/services/util.service';
import {Enlist} from '../../../shared/models/enlist';

@Component({
  selector: 'jwjh-enlist-update-form',
  templateUrl: './enlist-update-form.component.html',
  styleUrls: ['./enlist-update-form.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EnlistUpdateFormComponent {
  @Output()
  readonly onSubmit = new EventEmitter<Enlist>();
  readonly enlistForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.enlistForm = fb.group({
      'id': null,
      'paymentMerchant': [null, Validators.required],
      'title': ['', Validators.required],
      'money': [0, [Validators.required, Validators.min(0)]],
      'feedbackLimit': [0, [Validators.required, Validators.min(0)]],
      'content': ['', Validators.required],
      'attachments': [null],
      'feedbackTemplate': [null, Validators.required],
      'startDate': [new Date(), Validators.required],
    });
  }

  private _enlist: Enlist;

  get enlist(): Enlist {
    return this._enlist;
  }

  @Input()
  set enlist(value: Enlist) {
    this._enlist = Enlist.assign(value);
    if (!isDate(this._enlist.startDate)) {
      this._enlist.startDate = toDate(this._enlist.startDate);
    }
    this.enlistForm.patchValue(this._enlist);
    if (this._enlist.id) {
      this.money.disable();
    }
  }

  get id() {
    return this.enlistForm.get('id');
  }

  get paymentMerchant() {
    return this.enlistForm.get('paymentMerchant');
  }

  get title() {
    return this.enlistForm.get('title');
  }

  get money() {
    return this.enlistForm.get('money');
  }

  get feedbackLimit() {
    return this.enlistForm.get('feedbackLimit');
  }

  get feedbackTemplate() {
    return this.enlistForm.get('feedbackTemplate');
  }

  submit() {
    this.onSubmit.emit(this.enlistForm.value);
  }

}
