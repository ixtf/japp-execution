import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
import {ROUTER_NAVIGATION, RouterNavigationAction} from '@ngrx/router-store';
import {Action, Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {RedEnvelopeOrganization} from '../../../shared/models/red-envelope-organization';
import {RedEnvelopeOrganizationService} from '../../services/red-envelope-organization.service';
import {redEnvelopeOrganizationUpdatePageActions, redEnvelopeOrganizationUpdatePageState} from '../../store';

@Injectable()
export class RedEnvelopeOrganizationUpdatePageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType<RouterNavigationAction>(ROUTER_NAVIGATION)
    .filter(action => {
      const {event} = action.payload;
      return event.url.startsWith('/admin/redEnvelopeOrganizationEdit');
    })
    .withLatestFrom(this.store.select(redEnvelopeOrganizationUpdatePageState))
    .switchMap(a => {
      const [action, state] = a;
      const id = action.payload.event.state.root.queryParams.id;
      const redEnvelopeOrganization$ = id ? this.redEnvelopeOrganizationService.get(id) : of(new RedEnvelopeOrganization());
      return redEnvelopeOrganization$
        .map(res => new redEnvelopeOrganizationUpdatePageActions.InitSuccess(res))
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
