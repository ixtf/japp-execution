import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {ObservableMedia} from '@angular/flex-layout';
import {MatDialog, MatSnackBar, MatSnackBarConfig} from '@angular/material';
import {Store} from '@ngrx/store';
import {TranslateService} from '@ngx-translate/core';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/toPromise';
import {Observable} from 'rxjs/Observable';
import {merge} from 'rxjs/observable/merge';
import {of} from 'rxjs/observable/of';
import {Subject} from 'rxjs/Subject';
import {isArray, isNullOrUndefined, isString} from 'util';
import {baseApiUrl} from '../../../environments/environment';
import {UploadFile} from '../../shared/models/upload-file';
import {ConfirmDialogComponent} from '../components/confirm-dialog/confirm-dialog.component';
import {WxQrcodeDialogComponent} from '../components/wx-qrcode-dialog/wx-qrcode-dialog.component';
import {ConfirmOption} from '../models/confirm-option';
import {coreActions} from '../store';

@Injectable()
export class UtilService {
  constructor(private http: HttpClient,
              private store: Store<any>,
              private translate: TranslateService,
              private media$: ObservableMedia,
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {
    merge(of(null), media$.asObservable())
      .switchMap(() => of(this.isMobile()))
      .debounceTime(300)
      .distinctUntilChanged()
      .do((isMobile) => store.dispatch(new coreActions.SetIsMobile(isMobile)))
      .subscribe();
  }

  isMobile(): boolean {
    return this.media$.isActive('xs');
  }

  wxJsConfig(jsApiList: string[] = []): Observable<any> {
    const ready = new Subject();
    let href = isIos() ? sessionStorage.getItem('weixin_first_url') : location.href;
    href = location.href;
    const configFun = () => this.http.post(`${baseApiUrl}/auth/wxJsConfig`, {data: href}).toPromise().then((config: any) => {
      console.log('test', config);
      wx.config({
        appId: config.appId,
        timestamp: config.timestamp,
        nonceStr: config.nonceStr,
        signature: config.signature,
        jsApiList: [
          'onMenuShareTimeline',
          'onMenuShareAppMessage',
          'onMenuShareQQ',
          'onMenuShareWeibo',
          'onMenuShareQZone',
          'hideOptionMenu',
          'showOptionMenu',
          'hideMenuItems',
          'showMenuItems',
          ...jsApiList
        ]
      });
    }, err => {
      ready.error(err);
      ready.complete();
    });
    configFun();
    wx.ready(() => {
      ready.next();
      ready.complete();
    });
    wx.error(err => {
      ready.error(err);
      ready.complete();
      alert(JSON.stringify(err));
    });
    return ready;
  }

  onWxShareData(shareData: any) {
    wx.onMenuShareTimeline(shareData);
    wx.onMenuShareAppMessage(shareData);
    wx.onMenuShareQQ(shareData);
    wx.onMenuShareWeibo(shareData);
    wx.onMenuShareQZone(shareData);
  }

  showSuccess(message?: string, action?: string, config?: MatSnackBarConfig) {
    config = config || {duration: 3000};
    message = message || 'TOAST.SUCCESS';
    this.translate.get(message).subscribe(res => this.snackBar.open(res, action, config));
  }

  showError(err: string | any, action?: string, config?: MatSnackBarConfig) {
    console.log(err);
    if (isString(err)) {
      this.translate.get(err).subscribe(res => this.snackBar.open(res, action || 'X', config));
      return;
    }
  }

  showConfirm(option?: ConfirmOption): Observable<any> {
    const data = Object.assign({
      textContent: 'COMMON.DELETE_CONFIRM',
      okText: 'COMMON.CONFIRM',
      cancelText: 'COMMON.CANCEL'
    }, option);
    return this.dialog.open(ConfirmDialogComponent, {data})
      .afterClosed().filter(res => res);
  }

  showWxQrcode(ticket: string, option?: any) {
    const data = Object.assign({ticket}, option);
    return this.dialog.open(WxQrcodeDialogComponent, {panelClass: 'my-dialog', data});
  }

}

export const deleteEle = <T>(array: T[], ele: T): T[] => {
  if (!array || !ele || !isArray(array)) {
    return array;
  }
  return array.filter(it => {
    if (it === ele) {
      return false;
    }
    const id = it && it['id'];
    if (id === ele['id']) {
      return false;
    }
    return true;
  });
};

export const updateEle = <T>(array: T[], ele: T): T[] => {
  array = [...(array || [])];
  const i = array.findIndex(it => it === ele || (it && it['id']) === ele['id']);
  if (i < 0) {
    return array.concat([ele]);
  }
  ele = Object.assign(array[i], ele);
  array.splice(i, 1, ele);
  return array;
};

export const upEle = <T>(array: T[], ele: T): T[] => {
  if (!array || !ele || !isArray(array)) {
    return array;
  }
  array = [...(array || [])];
  const i = array.findIndex(it => it === ele || (it && it['id']) === ele['id']);
  if (i <= 0) {
    return;
  }
  array[i] = array[i - 1];
  array[i - 1] = ele;
  return array;
};

export const downEle = <T>(array: T[], ele: T): T[] => {
  if (!array || !ele || !isArray(array)) {
    return array;
  }
  array = [...(array || [])];
  const i = array.findIndex(it => it === ele || (it && it['id']) === ele['id']);
  if (i <= 0) {
    return;
  }
  array[i] = array[i + 1];
  array[i + 1] = ele;
  return array;
};

export const defaultValue = (v: any, d: any): any => {
  return isNullOrUndefined(v) ? d : v;
};

export const CheckQ = (sV: string, qV: string): boolean => {
  let s = sV || '';
  s = s.toLocaleLowerCase();
  if (qV) {
    const q = qV.toLocaleLowerCase();
    const b = s.indexOf(q) > -1;
    if (!b) {
      return false;
    }
  }
  return true;
};

export const DefaultCompare = (o1: any, o2: any): number => {
  if (o1.id === '0') {
    return -1;
  }
  if (o2.id === '0') {
    return 1;
  }
  return moment(o1.modifyDateTime).isAfter(o2.modifyDateTime) ? -1 : 1;
};

export const toDate = (v: any): Date => {
  return v && moment(v).toDate();
};

export const isWeixinBrowser = (): boolean => {
  return /micromessenger/i.test(navigator.userAgent);
};

export const isIos = (): boolean => {
  return !!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
};

export const isImage = (file: UploadFile): boolean => {
  return /\.(jpe?g|gif|png|bmp|webp)$/i.test(file.fileName);
};

export const isVideo = (file: UploadFile): boolean => {
  return /\.(avi|mp4)$/i.test(file.fileName);
};

export const isPdf = (file: UploadFile): boolean => {
  return /\.(pdf)$/i.test(file.fileName);
};

export const isWord = (file: UploadFile): boolean => {
  return /\.(docx)$/i.test(file.fileName);
};
