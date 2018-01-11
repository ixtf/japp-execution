import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {Enlist} from '../../../shared/models/enlist';
import {enlistUpdatePageActions, enlistUpdatePageEnlist, enlistUpdatePageTitle} from '../../store';

@Component({
  selector: 'jwjh-enlist-update-page',
  templateUrl: './enlist-update-page.component.html',
  styleUrls: ['./enlist-update-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EnlistUpdatePageComponent implements OnDestroy {
  title$: Observable<string>;
  enlist$: Observable<Enlist>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private route: ActivatedRoute) {
    this.title$ = this.store.select(enlistUpdatePageTitle);
    this.enlist$ = this.store.select(enlistUpdatePageEnlist);

    this.subscriptions.push(
      route.queryParams.subscribe((queryParams) => {
        return store.dispatch(new enlistUpdatePageActions.Init({
          paymentMerchantId: queryParams.paymentMerchantId,
          enlistId: queryParams.enlistId
        }));
      })
    );
  }

  save(enlist: Enlist) {
    this.store.dispatch(new enlistUpdatePageActions.Save(enlist));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
