import {Injectable} from '@angular/core';
import {CanActivate} from '@angular/router';
import {isWeixinBrowser, UtilService} from './util.service';

@Injectable()
export class WeixinGuard implements CanActivate {
  constructor(private utilService: UtilService) {
  }

  canActivate(): boolean {
    const result = isWeixinBrowser();
    if (!result) {
      this.utilService.showError('TOOLTIP.WEIXIN-GUARD');
    }
    return result;
  }
}
