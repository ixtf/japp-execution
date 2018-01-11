import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/scan';
import {Observable} from 'rxjs/Observable';
import {combineLatest} from 'rxjs/observable/combineLatest';
import {coreIsMobile} from '../../../core/store';
import {Enlist} from '../../../shared/models/enlist';
import {enlistProgressPageActions, enlistProgressPageEnlist, enlistProgressPageEnlists} from '../../store';

@Component({
  moduleId: module.id,
  selector: 'jwjh-enlist-progress-page',
  templateUrl: './enlist-progress-page.component.html',
  styleUrls: ['./enlist-progress-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EnlistProgressPageComponent implements OnDestroy {
  enlists$: Observable<Enlist[]>;
  enlist$: Observable<Enlist>;
  leftFlex$: Observable<number>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private route: ActivatedRoute) {
    this.enlists$ = store.select(enlistProgressPageEnlists);
    this.enlist$ = store.select(enlistProgressPageEnlist);

    this.leftFlex$ = combineLatest(this.enlist$, store.select(coreIsMobile), (enlist, isMobile) => {
      if (!isMobile) {
        return 40;
      }
      if (enlist) {
        return 0;
      }
      return 100;
    });

    this.subscriptions.push(
      route.queryParams.scan((acc, cur) => {
        store.dispatch(new enlistProgressPageActions.Init(acc, cur));
        return cur;
      }, null).subscribe(),
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
