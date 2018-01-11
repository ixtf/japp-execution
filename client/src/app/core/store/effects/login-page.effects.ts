import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import {of} from 'rxjs/observable/of';
import {AuthService} from '../../services/auth.service';
import {coreActions, loginPageActions} from '../actions';

@Injectable()
export class LoginPageEffects {
  @Effect()
  login$ = this.actions$
    .ofType(loginPageActions.LOGIN)
    .exhaustMap((action: loginPageActions.Login) => {
      return this.authService.login(action.authenticate)
        .map(res => new loginPageActions.LoginSuccess(res))
        .catch(error => of(new coreActions.ShowError(error)));
    });

  @Effect({dispatch: false})
  loginSuccess$ = this.actions$
    .ofType(loginPageActions.LOGIN_SUCCESS)
    .do(() => this.authService.navigateAfterLoginSuccess());

  constructor(private actions$: Actions, private authService: AuthService) {
  }

}
