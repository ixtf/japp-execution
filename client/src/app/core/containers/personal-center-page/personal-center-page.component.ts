import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {Operator} from '../../../shared/models/operator';
import {personalCenterPageActions, personalCenterPageOperator} from '../../store';

@Component({
  selector: 'jwjh-personal-center-page',
  templateUrl: './personal-center-page.component.html',
  styleUrls: ['./personal-center-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersonalCenterPageComponent implements OnDestroy {
  operator$: Observable<Operator>;
  private subscriptions = [];

  constructor(private store: Store<any>) {
    this.operator$ = this.store.select(personalCenterPageOperator);
    store.dispatch(new personalCenterPageActions.Init());
  }

  updatInfo(v: any): void {
    this.store.dispatch(new personalCenterPageActions.Update(v));
  }

  updatPassword(v: { oldPassword: string, loginPassword: string, loginPasswordAgain: string }): void {
    this.store.dispatch(new personalCenterPageActions.UpdatePassword(v));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
