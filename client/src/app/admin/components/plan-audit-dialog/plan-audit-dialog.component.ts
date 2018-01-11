import {ChangeDetectionStrategy, Component, Inject, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Store} from '@ngrx/store';
import {ApiService} from '../../../core/services/api.service';
import {UtilService} from '../../../core/services/util.service';
import {Plan} from '../../../shared/models/plan';
import {planAuditPageActions} from '../../store';

@Component({
  selector: 'jwjh-plan-audit-dialog',
  templateUrl: './plan-audit-dialog.component.html',
  styleUrls: ['./plan-audit-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlanAuditDialogComponent implements OnDestroy {
  readonly plan: Plan;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private utilService: UtilService,
              public dialogRef: MatDialogRef<PlanAuditDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { plan: Plan },
              private apiService: ApiService) {
    this.plan = data.plan;
  }

  static open(dialog: MatDialog, data: { plan: Plan }) {
    dialog.open(PlanAuditDialogComponent, {panelClass: 'my-dialog', data});
  }

  audit(): void {
    this.utilService.showConfirm({textContent: '通过确认！'})
      .switchMap(() => this.apiService.auditPlan(this.plan.id))
      .subscribe(() => {
        this.store.dispatch(new planAuditPageActions.AuditSuccess({plan: this.plan}));
        this.utilService.showSuccess();
        this.dialogRef.close();
      }, error => {
        this.utilService.showError(error);
      });
  }

  unAudit(): void {
    this.utilService.showConfirm({textContent: '拒绝确认！'})
      .switchMap(() => this.apiService.unAuditPlan(this.plan.id))
      .subscribe(() => {
        this.store.dispatch(new planAuditPageActions.UnAuditSuccess({plan: this.plan}));
        this.utilService.showSuccess();
        this.dialogRef.close();
      }, error => {
        this.utilService.showError(error);
      });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
