import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {EnlistFeedback} from '../../shared/models/enlist-feedback';

@Injectable()
export class EnlistFeedbackService {

  constructor(private http: HttpClient, private store: Store<any>) {
  }

  save(enlistId: string, feedback: EnlistFeedback): Observable<EnlistFeedback> {
    return feedback.id ? this.update(feedback) : this.create(enlistId, feedback);
  }

  get(feedbackId: string): Observable<EnlistFeedback> {
    return this.http.get(`${baseApiUrl}/enlistFeedbacks/${feedbackId}`);
  }

  list(enlistId: string): Observable<EnlistFeedback[]> {
    return this.http.get(`${baseApiUrl}/enlists/${enlistId}/enlistFeedbacks`);
  }

  delete(feedbackId: string): Observable<any> {
    return this.http.delete(`${baseApiUrl}/enlistFeedbacks/${feedbackId}`);
  }

  private create(enlistId: string, feedback: EnlistFeedback): Observable<EnlistFeedback> {
    return this.http.post(`${baseApiUrl}/enlistFeedbacks?enlistId=${enlistId}`, feedback);
  }

  private update(feedback: EnlistFeedback): Observable<EnlistFeedback> {
    return this.http.put(`${baseApiUrl}/enlistFeedbacks/${feedback.id}`, feedback);
  }

}
