import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {Subscription} from 'rxjs/Subscription';
import {UtilService} from '../../../core/services/util.service';
import {WeixinPayService} from '../../services/weixin-pay.service';

@Component({
  selector: 'jwjh-payment-merchant-pay-page',
  templateUrl: './payment-merchant-pay-page.component.html',
  styleUrls: ['./payment-merchant-pay-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PaymentMerchantPayPageComponent implements OnDestroy {
  @HostBinding('class.jwjh-page') b1 = true;
  @HostBinding('class.jwjh-payment-merchant-pay-page') b2 = true;
  private readonly subscriptions: Subscription[] = [];

  constructor(private store: Store<any>,
              private route: ActivatedRoute,
              private router: Router,
              private utilService: UtilService,
              private weixinPayService: WeixinPayService) {
    this.subscriptions.push(
      this.utilService.wxJsConfig(['chooseWXPay'])
        .switchMap(() => this.route.queryParamMap)
        .switchMap(params => {
          const enlistFeedbackId = params.get('enlistFeedbackId');
          return this.weixinPayService.payDataEnlistFeedback(enlistFeedbackId);
        })
        .subscribe(res => this.pay(res))
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(it => it.unsubscribe());
  }

  private pay(payData: any) {
    wx.chooseWXPay({
      timestamp: payData.timeStamp,
      nonceStr: payData.nonceStr,
      package: payData.package,
      signType: 'MD5',
      paySign: payData.paySign,
      success: () => {
        this.router.navigate(['/enlists/progress']);
      },
      fail: (err) => {
        alert('支付失败！');
        alert(JSON.stringify(err));
      }
    });
  }
}
