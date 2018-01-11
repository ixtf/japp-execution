import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Router} from '@angular/router';
import {Actions, Effect} from '@ngrx/effects';
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
import {ChannelService} from '../../services/channel.service';
import {channelManagePageActions} from '../../store';

@Injectable()
export class ChannelManagePageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(channelManagePageActions.INIT)
    .exhaustMap(() => {
      return this.channelService.list()
        .map(res => new channelManagePageActions.InitSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private store: Store<any>,
              private actions$: Actions,
              private router: Router,
              private dialog: MatDialog,
              private utilService: UtilService,
              private channelService: ChannelService) {
  }
}
