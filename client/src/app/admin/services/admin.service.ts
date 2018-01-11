import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {Channel} from '../../shared/models/channel';
import {PaymentMerchant} from '../../shared/models/payment-merchant';

@Injectable()
export class AdminService {

  constructor() {
  }

  listChannel(): Observable<Channel[]> {
    return of();
  }

  listPaymentMerchant(): Observable<PaymentMerchant[]> {
    return of();
  }
}
