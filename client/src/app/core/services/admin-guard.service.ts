import {Injectable} from '@angular/core';
import {CanActivate} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/take';
import {Observable} from 'rxjs/Observable';
import {coreActions, coreIsAdmin} from '../store';

@Injectable()
export class AdminGuard implements CanActivate {
  constructor(private store: Store<any>) {
  }

  canActivate(): Observable<boolean> {
    return this.store.select(coreIsAdmin).map(authed => {
      if (!authed) {
        this.store.dispatch(new coreActions.LoginRedirect());
        return false;
      }
      return true;
    });
  }
}
