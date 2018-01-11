import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {WeixinService} from '../../core/services/weixin.service';

const baseUrl = `${baseApiUrl}/enlists`;

@Injectable()
export class WeixinPayService {

  constructor(private http: HttpClient, private weixinService: WeixinService, private store: Store<any>) {
  }

  payDataEnlistFeedback(feedbackId: string): Observable<any> {
    return this.http.get(`${baseApiUrl}/enlistFeedbacks/${feedbackId}/weixinPay`);
  }
}
