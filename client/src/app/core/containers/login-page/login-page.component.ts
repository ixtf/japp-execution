import {ChangeDetectionStrategy, Component, HostBinding} from '@angular/core';
import {Store} from '@ngrx/store';
import {Authenticate} from '../../../shared/models/authenticate';
import {loginPageActions} from '../../store/actions';

@Component({
  selector: 'jwjh-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginPageComponent {
  @HostBinding('class.jwjh-page') b1 = true;
  @HostBinding('class.jwjh-login-page') b2 = true;

  constructor(private store: Store<any>) {
  }

  login(authenticate: Authenticate) {
    this.store.dispatch(new loginPageActions.Login(authenticate));
  }

}
