import {Entity} from './entity';
import {WeixinPayment} from './weixin-payment';

export class EnlistFeedbackPayment extends Entity {
  successed: boolean;
  out_trade_no: string;
  weixinPayment: WeixinPayment;
}
