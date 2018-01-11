import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Operator} from '../../../shared/models/operator';

@Component({
  selector: 'jwjh-personal-update-form',
  templateUrl: './personal-update-form.component.html',
  styleUrls: ['./personal-update-form.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersonalUpdateFormComponent implements OnInit {
  readonly infoForm: FormGroup;
  readonly passwordForm: FormGroup;
  @Input()
  operator: Operator;
  @Output()
  onUpdatInfo = new EventEmitter<{ name: string, mobile?: string }>();
  @Output()
  onUpdatPassword = new EventEmitter<{ oldPassword: string, loginPassword: string, loginPasswordAgain: string }>();

  constructor(private fb: FormBuilder) {
    this.infoForm = this.fb.group({
      name: ['', Validators.required],
      mobile: '',
    });
    this.passwordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      loginPassword: ['', Validators.required],
      loginPasswordAgain: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.infoForm.patchValue(this.operator);
  }

  updateInfo(): void {
    this.onUpdatInfo.emit(this.infoForm.value);
  }

}
