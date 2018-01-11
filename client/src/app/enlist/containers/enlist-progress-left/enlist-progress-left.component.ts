import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatDialog} from '@angular/material';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {coreAuthOperator} from '../../../core/store/index';
import {Enlist} from '../../../shared/models/enlist';
import {EnlistFeedbackUpdateDialogComponent} from '../../components/enlist-feedback-update-dialog/enlist-feedback-update-dialog.component';
import {EnlistService} from '../../services/enlist.service';
import {enlistProgressPageEnlistId, enlistProgressPageEnlists} from '../../store';

@Component({
  selector: 'jwjh-enlist-progress-left',
  templateUrl: './enlist-progress-left.component.html',
  styleUrls: ['./enlist-progress-left.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EnlistProgressLeftComponent {
  enlists$: Observable<Enlist[]>;
  selectedId$: Observable<string>;

  constructor(private store: Store<any>,
              private router: Router,
              private dialog: MatDialog,
              private route: ActivatedRoute,
              private enlistService: EnlistService) {
    this.enlists$ = store.select(enlistProgressPageEnlists);
    this.selectedId$ = this.store.select(enlistProgressPageEnlistId);
  }

  isShowFeedbackBtn$(enlist: Enlist): Observable<boolean> {
    return this.store.select(coreAuthOperator).take(1).map(authOperator => {
      const find = (enlist.enlistFeedbacks || []).find(it => it.creator.id === authOperator.id);
      return !find;
    });
  }

  select(enlist: Enlist) {
    this.router.navigate(['/enlists/progress'], {queryParams: {enlistId: enlist.id}});
  }

  updateEnlistFeedback(enlist: Enlist) {
    EnlistFeedbackUpdateDialogComponent.open(this.dialog, {enlist});
  }

}
