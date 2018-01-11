import {AfterViewInit, ChangeDetectionStrategy, Component} from '@angular/core';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'jwjh-login-weixin',
  templateUrl: './login-weixin.component.html',
  styleUrls: ['./login-weixin.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginWeixinComponent implements AfterViewInit {
  private WxLoginConfig = {
    id: 'WxLoginDiv',
    appid: 'wxde41076df5c1df83',
    scope: 'snsapi_login',
    redirect_uri: 'http://www.wjh001.com/execution/oauth/weixin?oauth=WX_OPEN'
  };

  constructor(private authService: AuthService) {
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      const o = new WxLogin(this.WxLoginConfig);
    }, 1000);
  }

}
