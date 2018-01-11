import {ChangeDetectionStrategy, Component, Input, OnDestroy} from '@angular/core';
import {MatDialog, MatTabChangeEvent} from '@angular/material';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import {Observable} from 'rxjs/Observable';
import {UtilService} from '../../../core/services/util.service';
import {coreAuthOperator} from '../../../core/store';
import {Enlist} from '../../../shared/models/enlist';
import {EnlistFeedback} from '../../../shared/models/enlist-feedback';
import {EnlistFeedbackUpdateDialogComponent} from '../../components/enlist-feedback-update-dialog/enlist-feedback-update-dialog.component';
import {EnlistGenerateTaskDialogComponent} from '../../components/enlist-generate-task-dialog/enlist-generate-task-dialog.component';
import {EnlistOperatorDialogComponent} from '../../components/enlist-operator-dialog/enlist-operator-dialog.component';
import {
  enlistActions, enlistFeedbackActions, enlistProgressPageActions, enlistProgressPageEnlistFeedbacks,
  enlistProgressPageStateRightTabIndex
} from '../../store';

@Component({
  selector: 'jwjh-enlist-progress-right',
  templateUrl: './enlist-progress-right.component.html',
  styleUrls: ['./enlist-progress-right.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EnlistProgressRightComponent implements OnDestroy {
  @Input()
  enlist: Enlist;
  enlistFeedbacks$: Observable<EnlistFeedback[]>;
  rightTabIndex$: Observable<number>;
  private subscriptions = [];

  constructor(private store: Store<any>,
              private dialog: MatDialog,
              private utilService: UtilService) {
    this.enlistFeedbacks$ = store.select(enlistProgressPageEnlistFeedbacks);
    this.rightTabIndex$ = store.select(enlistProgressPageStateRightTabIndex);
  }

  get isShowViewMore$() {
    return this.rightTabIndex$.map(i => {
      if (i === 0) {
        return false;
      }
      if (!this.enlist.content) {
        return false;
      }
      return true;
    });
  }

  get isManager$(): Observable<boolean> {
    return this.store.select(coreAuthOperator).take(1).map(it => this.enlist.isManager(it));
  }

  get isShowFeedbackBtn$(): Observable<boolean> {
    return this.store.select(coreAuthOperator).take(1).map(authOperator => {
      const enlistFeedbacks = this.enlist && this.enlist.enlistFeedbacks || [];
      const find = enlistFeedbacks.find(it => it.creator.id === authOperator.id);
      return !find;
    });
  }

  focusChange(ev: MatTabChangeEvent) {
    this.store.dispatch(new enlistProgressPageActions.SetRightTabIndex(ev.index));
  }

  viewMore() {
    this.store.dispatch(new enlistProgressPageActions.SetRightTabIndex(0));
  }

  showParticipants() {
    EnlistOperatorDialogComponent.showParticipants(this.dialog, {enlist: this.enlist});
  }

  deleteFeedback(taskFeedback: EnlistFeedback) {
    this.utilService.showConfirm()
      .subscribe(() => this.store.dispatch(new enlistFeedbackActions.Delete(this.enlist.id, taskFeedback.id)));
  }

  invite() {
    this.store.dispatch(new enlistActions.Invite(this.enlist.id));
  }

  done() {
    this.utilService.showConfirm().subscribe(() => this.store.dispatch(new enlistActions.Done(this.enlist.id)));
  }

  generateTask() {
    EnlistGenerateTaskDialogComponent.open(this.dialog, {enlist: this.enlist});
  }

  updateFeedback(enlistFeedback?: EnlistFeedback) {
    EnlistFeedbackUpdateDialogComponent.open(this.dialog, {enlist: this.enlist, enlistFeedback});
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
