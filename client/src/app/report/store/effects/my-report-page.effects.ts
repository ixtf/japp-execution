import {Injectable} from '@angular/core';
import {Actions, Effect} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/exhaustMap';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/pluck';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {myReportPageActions} from '../';
import {UtilService} from '../../../core/services/util.service';
import {ShowError} from '../../../core/store/actions/core';
import {ReportService} from '../../services/report.service';

@Injectable()
export class MyReportPageEffects {

  @Effect()
  list$: Observable<Action> = this.actions$
    .ofType(myReportPageActions.INIT)
    .exhaustMap(() => {
      return this.reportService.fetchMyReport()
        .map(res => new myReportPageActions.InitSuccess(res))
        .catch(error => of(new ShowError(error)));
    });

  constructor(private actions$: Actions,
              private utilService: UtilService,
              private reportService: ReportService) {
  }
}
