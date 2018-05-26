import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Action, Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {UtilService} from '../../../core/services/util.service';
import {ShowError, ShowWxQrcoode} from '../../../core/store/actions/core';
import {RedEnvelopeOrganizationService} from '../../services/red-envelope-organization.service';
import {redEnvelopeOrganizationManagePageActions} from '../../store';
import {redEnvelopeOrganizationManagePageState} from '../index';

@Injectable()
export class RedEnvelopeOrganizationManagePageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .filter(action => {
      const {event} = action.payload;
      return event.url.startsWith('/admin/redEnvelopeOrganizations');
    })
    .withLatestFrom(this.store.select(redEnvelopeOrganizationManagePageState))
    .switchMap(a => {
      const [action, state] = a;
      return this.redEnvelopeOrganizationService.list()
        .map(res => new redEnvelopeOrganizationManagePageActions.InitSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  invite$: Observable<Action> = this.actions$
    .ofType(redEnvelopeOrganizationManagePageActions.INVITE)
    .exhaustMap((action: redEnvelopeOrganizationManagePageActions.Invite) => {
      return this.redEnvelopeOrganizationService.invite(action.redEnvelopeOrganizationId)
        .map(res => new ShowWxQrcoode(res.ticket, ''))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private dialog: MatDialog,
              private utilService: UtilService,
              private redEnvelopeOrganizationService: RedEnvelopeOrganizationService) {
  }
}
