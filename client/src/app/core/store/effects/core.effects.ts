import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Router, RouterStateSnapshot} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {WxQrcodeDialogComponent} from '../../components/wx-qrcode-dialog/wx-qrcode-dialog.component';
import {AuthService} from '../../services/auth.service';
import {UtilService} from '../../services/util.service';
import {coreActions} from '../actions';

@Injectable()
export class CoreEffects {
  @Effect()
  route$: Observable<Action> = this.actions$
    .ofType(ROUTER_NAVIGATION)
    .exhaustMap((action: RouterNavigationAction<RouterStateSnapshot>) => {
      const actions: Action[] = [];
      const url = action.payload.routerState.url || '';
      if (url.startsWith('/login') || url.startsWith('/shareTimeline')) {
        actions.push(new coreActions.SetShowSidenav(false));
        actions.push(new coreActions.SetShowToolbar(false));
      } else {
        actions.push(new coreActions.SetShowSidenav(true));
        actions.push(new coreActions.SetShowToolbar(true));
      }
      return actions;
    });

  @Effect({dispatch: false})
  loginRedirect$ = this.actions$
    .ofType(coreActions.LOGIN_REDIRECT)
    .do(() => this.router.navigate(['login']));

  @Effect({dispatch: false})
  imagePreview$ = this.actions$
    .ofType(coreActions.IMAGE_PREVIEW)
    .do(() => this.authService.logout())
    .map(() => new coreActions.LoginRedirect());

  @Effect()
  showError$: Observable<Action> = this.actions$
    .ofType(coreActions.SHOW_ERROR)
    .exhaustMap((action: any) => {
      this.utilService.showError(action.payload);
      return of();
    });

  @Effect()
  showWxQrcoode$: Observable<Action> = this.actions$
    .ofType(coreActions.SHOW_WX_QRCOODE)
    .exhaustMap((data: any) => {
      this.dialog.open(WxQrcodeDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
      return of();
    });

  constructor(private actions$: Actions,
              private router: Router,
              private dialog: MatDialog,
              private authService: AuthService,
              private utilService: UtilService) {
  }

}
