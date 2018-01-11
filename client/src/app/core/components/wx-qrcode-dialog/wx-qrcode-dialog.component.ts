import {ChangeDetectionStrategy, Component, HostBinding, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';

@Component({
  selector: 'jwjh-wx-qrcode-dialog',
  templateUrl: './wx-qrcode-dialog.component.html',
  styleUrls: ['./wx-qrcode-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WxQrcodeDialogComponent {
  @HostBinding('class.my-dialog-component') b1 = true;
  @HostBinding('class.wx-qrcode-dialog') b2 = true;
  readonly url: string;
  readonly title: string;

  constructor(public dialogRef: MatDialogRef<WxQrcodeDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.url = `https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${data.ticket}`;
    this.title = data.title;
  }

  static openByWeixin(dialog: MatDialog, data: { ticket: string, title: string }) {
    dialog.open(WxQrcodeDialogComponent, {disableClose: true, panelClass: 'my-dialog', data});
  }
}
