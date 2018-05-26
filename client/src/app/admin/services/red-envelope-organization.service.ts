import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {CurdService} from '../../core/services/curd.service';
import {PaymentMerchant} from '../../shared/models/payment-merchant';
import {PaymentMerchantInvite} from '../../shared/models/payment-merchant-invite';

@Injectable()
export class RedEnvelopeOrganizationService extends CurdService<PaymentMerchant, string> {
  constructor(http: HttpClient, private store: Store<any>) {
    super(http, 'admin/redEnvelopeOrganizations');
  }

  invite(redEnvelopeOrganizationId: string): Observable<PaymentMerchantInvite> {
    return this.http.get(`${baseApiUrl}/redEnvelopeOrganizations/${redEnvelopeOrganizationId}/inviteTicket`);
  }
}
