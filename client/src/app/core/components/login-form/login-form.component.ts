import {ChangeDetectionStrategy, Component, EventEmitter, Output} from '@angular/core';
import {Authenticate} from '../../../shared/models/authenticate';

@Component({
  selector: 'jwjh-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginFormComponent {
  @Output()
  submitted = new EventEmitter<Authenticate>();
  authenticate = {loginId: '', loginPassword: ''};

  submit() {
    this.submitted.emit(this.authenticate);
  }

}
