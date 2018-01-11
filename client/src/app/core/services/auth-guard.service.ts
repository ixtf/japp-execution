import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/operator/take';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {coreActions, coreAuthOperator, loginPageActions} from '../store';
import {AuthService} from './auth.service';

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private store: Store<any>, private authService: AuthService) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.store.select(coreAuthOperator)
      .switchMap(authOperator => {
        if (authOperator) {
          return of(true);
        }

        this.authService.setAfterLoginSuccess(next, state);
        return this.authService.get()
          .do(res => this.store.dispatch(new loginPageActions.LoginSuccess(res)))
          .map(res => true)
          .catch(() => {
            this.store.dispatch(new coreActions.LoginRedirect());
            return of(false);
          });
      });
  }

}
