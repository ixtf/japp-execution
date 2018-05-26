import {Injectable} from '@angular/core';
import {CanActivate} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/take';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {coreActions, coreIsAdmin, loginPageActions} from '../store';
import {AuthService} from './auth.service';

@Injectable()
export class AdminGuard implements CanActivate {
  constructor(private store: Store<any>, private authService: AuthService) {
  }

  canActivate(): Observable<boolean> {
    return this.store.select(coreIsAdmin).switchMap(authed => {
      if (authed) {
        return of(true);
      }

      return this.authService.get()
        .do(res => this.store.dispatch(new loginPageActions.LoginSuccess(res)))
        .map(res => res.isAdmin)
        .catch(() => {
          this.store.dispatch(new coreActions.LoginRedirect());
          return of(false);
        });
    });
  }
}
