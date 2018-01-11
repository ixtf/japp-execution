import {ChangeDetectionStrategy, Component, Inject, OnDestroy} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Actions} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {UtilService} from '../../../core/services/util.service';
import {Enlist} from '../../../shared/models/enlist';
import {Operator} from '../../../shared/models/operator';
import {enlistActions} from '../../store';

@Component({
  selector: 'jwjh-enlist-operator-dialog',
  templateUrl: './enlist-operator-dialog.component.html',
  styleUrls: ['./enlist-operator-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EnlistOperatorDialogComponent implements OnDestroy {
  enlist$ = new BehaviorSubject<Enlist>(new Enlist());
  private subscriptions = [];

  constructor(private actions$: Actions,
              private store: Store<any>,
              private utilService: UtilService,
              public dialogRef: MatDialogRef<EnlistOperatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { enlist: Enlist, selectedIndex?: number }) {
    this.enlist$.next(data.enlist);

    this.subscriptions.push(
      actions$.ofType(enlistActions.GET_SUCCESS)
        .subscribe((action: any) => {
          if (data.enlist.id === action.task.id) {
            this.enlist$.next(Enlist.assign(action.enlist));
          }
        })
    );
  }

  static showParticipants(dialog: MatDialog, data: { enlist: Enlist }): MatDialogRef<EnlistOperatorDialogComponent> {
    return dialog.open(EnlistOperatorDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  deleteParticipant(operator: Operator) {
    const action = new enlistActions.DeleteParticipant({enlistId: this.enlist$.value.id, participantId: operator.id});
    this.utilService.showConfirm().subscribe(() => this.store.dispatch(action));
  }

  deleteFollower(operator: Operator) {
    const action = new enlistActions.DeleteManager({enlistId: this.enlist$.value.id, managerId: operator.id});
    this.utilService.showConfirm().subscribe(() => this.store.dispatch(action));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

}
