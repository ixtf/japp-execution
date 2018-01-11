import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {Subscription} from 'rxjs/Subscription';
import {ApiService} from '../../../core/services/api.service';
import {ShowWxQrcoode} from '../../../core/store/actions/core';
import {Plan} from '../../../shared/models/plan';
import {planManagePageAudited, planManagePageDraft, planManagePagePlans, planManagePagePublished} from '../../store';

@Component({
  selector: 'jwjh-plan-manage-page',
  templateUrl: './plan-manage-page.component.html',
  styleUrls: ['./plan-manage-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlanManagePageComponent implements OnDestroy {
  audited$: Observable<boolean>;
  published$: Observable<boolean>;
  draft$: Observable<boolean>;
  plans$: Observable<Plan[]>;
  private subscriptions: Subscription[] = [];

  constructor(private store: Store<any>,
              private apiService: ApiService) {
    this.audited$ = this.store.select(planManagePageAudited);
    this.published$ = this.store.select(planManagePagePublished);
    this.draft$ = this.store.select(planManagePageDraft);
    this.plans$ = this.store.select(planManagePagePlans);
  }

  invite(plan: Plan): void {
    this.apiService.planInvite(plan.id)
      .subscribe(res => {
        this.store.dispatch(new ShowWxQrcoode(res.ticket, ''));
      });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
