import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/toPromise';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {baseApiUrl} from '../../../environments/environment';

@Injectable()
export class WeixinService {
  private readonly ready$: Observable<any>;

  constructor(private http: HttpClient) {
    sessionStorage.setItem('weixin_first_url', location.href);
    this.ready$ = Observable.create(observer => {
      const callback = () => {
        observer.next({appid: 'wx3902a712d038c298'});
        observer.complete();
      };
      if (typeof WeixinJSBridge === 'object' && typeof WeixinJSBridge.invoke === 'function') {
        callback();
      } else {
        if (document.addEventListener) {
          document.addEventListener('WeixinJSBridgeReady', callback, false);
        } else if (document['attachEvent']) {
          document['attachEvent']('WeixinJSBridgeReady', callback);
          document['attachEvent']('onWeixinJSBridgeReady', callback);
        }
      }
    });
  }

  imagePreview(data: { current: string, urls?: string[] }) {
    WeixinJSBridge.invoke('imagePreview', {
      current: 'http://img1.gtimg.com/10/1048/104857/10485726_980x1200_0.jpg',
      urls: [ // 所有图片的url列表，数组格式
        'http://img1.gtimg.com/10/1048/104857/10485731_980x1200_0.jpg',
        'http://img1.gtimg.com/10/1048/104857/10485726_980x1200_0.jpg',
        'http://img1.gtimg.com/10/1048/104857/10485729_980x1200_0.jpg'
      ]
    }, function (res) {
    });
  }

  shareTimeline(data: { title: string, desc?: string, link: string, img_url: string, img_width?: number, img_height?: number }): Observable<any> {
    return this.ready$.switchMap(() => {
      console.log('test', 'start shareTimeline');
      console.log('test', data);
      const result$ = new Subject();
      data = Object.assign({appid: 'wx3902a712d038c298', img_width: 550, img_height: 370}, data);
      data.desc = data.desc || data.title;
      WeixinJSBridge.invoke('shareTimeline', data, res => {
        alert(JSON.stringify(res));
        if (res.err_msg === 'shareTimeline:ok') {
          result$.next();
        } else {
          result$.error(res);
        }
        result$.complete();
        console.log('test', 'end shareTimeline');
      });
      return result$;
    });
  }

  d(data: { title: string, desc?: string, link: string, img_url: string, img_width: number, img_height: number }): Observable<any> {
    return this.ready$.switchMap(() => {
      console.log('test', 'start shareTimeline');
      console.log('test', data);
      const result$ = new Subject();
      WeixinJSBridge.on('menu:share:timeline', res => {
        alert('menu:share:timeline');
        res = res || {};
        console.log('test', res);
        if (res.err_msg === 'shareTimeline:ok') {
          result$.next();
        } else {
          result$.error(res);
        }
        result$.complete();
        console.log('test', 'end shareTimeline');
      });
      return result$;
    });
  }

  getBrandWCPayRequest(data: any): Observable<any> {
    return this.ready$.switchMap(() => {
      const result$ = new Subject();
      WeixinJSBridge.invoke('getBrandWCPayRequest', data, res => {
        if (res.err_msg === 'get_brand_wcpay_request:ok') {
          result$.next();
        } else {
          result$.error(res);
        }
        result$.complete();
      });
      return result$;
    });
  }

  payEnlistFeedback(feedbackId: string): Observable<any> {
    return this.http.get(`${baseApiUrl}/enlistFeedbacks/${feedbackId}/weixinPay`);
  }
}
