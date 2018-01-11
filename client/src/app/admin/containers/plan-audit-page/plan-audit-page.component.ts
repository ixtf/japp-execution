import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {ApiService} from '../../../core/services/api.service';
import {Plan} from '../../../shared/models/plan';
import {PlanAuditDialogComponent} from '../../components/plan-audit-dialog/plan-audit-dialog.component';
import {planAuditPagePlans} from '../../store';

@Component({
  selector: 'jwjh-plan-audit-page',
  templateUrl: './plan-audit-page.component.html',
  styleUrls: ['./plan-audit-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlanAuditPageComponent {
  plans$: Observable<Plan[]>;

  constructor(private store: Store<any>,
              private dialog: MatDialog,
              private apiService: ApiService) {
    this.plans$ = this.store.select(planAuditPagePlans);
  }

  audit(plan: Plan) {
    PlanAuditDialogComponent.open(this.dialog, {plan});
  }

}
