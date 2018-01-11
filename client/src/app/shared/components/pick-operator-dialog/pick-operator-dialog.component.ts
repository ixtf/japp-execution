import {Component, Inject, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef, MatSelectionList} from '@angular/material';
import {Operator} from '../../models/operator';

@Component({
  selector: 'jwjh-pick-operator-dialog',
  templateUrl: './pick-operator-dialog.component.html',
  styleUrls: ['./pick-operator-dialog.component.less']
})
export class PickOperatorDialogComponent {
  @ViewChild('t001ls') selectionList: MatSelectionList;
  readonly multi: boolean;
  readonly dest: Operator[];
  readonly source: Operator[];

  constructor(private dialog: MatDialog,
              private dialogRef: MatDialogRef<PickOperatorDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: any) {
    this.multi = data.multi;
    this.source = data.source;
    if (this.multi) {
      this.dest = data.dest || [];
    }
  }

  static single(dialog: MatDialog, data: { source: Operator[]; }): MatDialogRef<PickOperatorDialogComponent> {
    data = Object.assign(data, {multi: false});
    return dialog.open(PickOperatorDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  static multi(dialog: MatDialog, data: { dest?: Operator[]; source: Operator[]; }): MatDialogRef<PickOperatorDialogComponent> {
    data = Object.assign(data, {multi: true});
    return dialog.open(PickOperatorDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }

  pick(operator: Operator) {
    this.dialogRef.close(operator);
  }

  pickMulti() {
    const selecteds = this.selectionList.selectedOptions.selected;
    if (selecteds.length > 0) {
      const t001ls = selecteds.map(it => it.value);
      this.dialogRef.close(t001ls);
    } else {
      this.dialogRef.close();
    }
  }
}
