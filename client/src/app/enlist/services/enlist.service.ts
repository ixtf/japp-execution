import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {baseApiUrl} from '../../../environments/environment';
import {coreAuthOperator} from '../../core/store/index';
import {Enlist} from '../../shared/models/enlist';
import {EnlistInvite} from '../../shared/models/enlist-invite';
import {Operator} from '../../shared/models/operator';
import {PaymentMerchant} from '../../shared/models/payment-merchant';
import {PaymentMerchantInvite} from '../../shared/models/payment-merchant-invite';
import {Task} from '../../shared/models/task';
import {TaskGroup} from '../../shared/models/task-group';

const baseUrl = `${baseApiUrl}/enlists`;

@Injectable()
export class EnlistService {

  constructor(private http: HttpClient, private store: Store<any>) {
  }

  save(o: Enlist): Observable<Enlist> {
    return o.id ? this.update(o) : this.create(o.paymentMerchant.id, o);
  }

  get(id: string): Observable<Enlist> {
    return this.http.get(`${baseUrl}/${id}`);
  }

  delete(id: string): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  list(): Observable<Enlist[]> {
    return this.http.get(`${baseUrl}`);
  }

  invite(id: string): Observable<EnlistInvite> {
    return this.http.get(`${baseUrl}/${id}/inviteTicket`);
  }

  listPaymentMerchant(): Observable<PaymentMerchant[]> {
    return this.http.get(`${baseApiUrl}/paymentMerchants`);
  }

  getPaymentMerchant(paymentMerchantId: string): Observable<PaymentMerchant> {
    return this.http.get(`${baseApiUrl}/paymentMerchants/${paymentMerchantId}`);
  }

  paymentMerchantInvite(paymentMerchantId: string): Observable<PaymentMerchantInvite> {
    return this.http.get(`${baseApiUrl}/paymentMerchants/${paymentMerchantId}/inviteTicket`);
  }

  deletePaymentMerchantManager(paymentMerchantId: string, managerId: string): Observable<PaymentMerchantInvite> {
    return this.http.delete(`${baseApiUrl}/paymentMerchants/${paymentMerchantId}/managers/${managerId}`);
  }

  listHistory(first: number, pageSize: number): Observable<{ count: number; enlists: Enlist[] }> {
    return this.http.get(`${baseApiUrl}/enlistHistory?first=${first}&pageSize=${pageSize}`);
  }

  done(enlistId: string): Observable<any> {
    return this.http.put(`${baseUrl}/${enlistId}/finish`, null);
  }

  restart(enlistId: string): Observable<any> {
    return this.http.delete(`${baseUrl}/${enlistId}/finish`);
  }

  hasEnlistFeedback(enlist: Enlist, o?: Operator): Observable<boolean> {
    if (!enlist) {
      return of(false);
    }
    const operator$ = o ? of(o) : this.store.select(coreAuthOperator).take(1);
    return operator$.map(operator => !!enlist.enlistFeedbacks.find(it => it.creator.id === o.id));
  }

  generateTask(enlistId: string, taskGroup: TaskGroup, taskGroupName: string): Observable<Task> {
    return this.http.post(`${baseUrl}/${enlistId}/task`, {taskGroup, taskGroupName});
  }

  private create(paymentMerchantId: string, o: Enlist): Observable<Enlist> {
    return this.http.post(`${baseUrl}?paymentMerchantId=${paymentMerchantId}`, o);
  }

  private update(o: Enlist): Observable<Enlist> {
    return this.http.put(`${baseUrl}/${o.id}`, o);
  }
}
