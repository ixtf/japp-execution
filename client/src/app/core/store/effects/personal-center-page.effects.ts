import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/take';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {coreAuthOperator, personalCenterPageActions} from '..';
import {baseApiUrl} from '../../../../environments/environment';
import {UtilService} from '../../services/util.service';
import {ShowError} from '../actions/core';

@Injectable()
export class PersonalCenterPageEffects {
  @Effect()
  init$: Observable<Action> = this.actions$
    .ofType(personalCenterPageActions.INIT)
    .exhaustMap((action: personalCenterPageActions.Init) => {
      return this.store.select(coreAuthOperator)
        .take(1)
        .map(res => new personalCenterPageActions.InitSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  updateInfo$: Observable<Action> = this.actions$
    .ofType(personalCenterPageActions.UPDATE)
    .exhaustMap((action: personalCenterPageActions.Update) => {
      return this.http.put(`${baseApiUrl}/me`, action.payload)
        .map(() => {
          this.utilService.showSuccess();
          return new personalCenterPageActions.UpdateSuccess(action.payload);
        })
        .catch(error => of(new ShowError(error)));
    });

  @Effect()
  updatePassword$: Observable<Action> = this.actions$
    .ofType(personalCenterPageActions.UPDATE_PASSWORD)
    .exhaustMap((action: personalCenterPageActions.UpdatePassword) => {
      return this.http.put(`${baseApiUrl}/me/password`, action.payload)
        .switchMap(() => {
          this.utilService.showSuccess();
          return of();
        })
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions,
              private http: HttpClient,
              private store: Store<any>,
              private utilService: UtilService) {
  }

}
