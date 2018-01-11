import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {ExamQuestion} from '../../shared/models/exam-question';

const baseUrl = `${baseApiUrl}/examQuestions`;

@Injectable()
export class ExamQuestionService {

  constructor(private http: HttpClient, private store: Store<any>) {
  }

  save(labId: string, o: ExamQuestion): Observable<ExamQuestion> {
    return o.id ? this.update(labId, o) : this.create(labId, o);
  }

  get(id: string): Observable<ExamQuestion> {
    return this.http.get(`${baseUrl}/${id}`);
  }

  delete(id: string): Observable<any> {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  private create(labId: string, o: ExamQuestion): Observable<ExamQuestion> {
    return this.http.post(`${baseUrl}?labId=${labId}`, o);
  }

  private update(labId: string, o: ExamQuestion): Observable<ExamQuestion> {
    return this.http.put(`${baseUrl}/${o.id}?labId=${labId}`, o);
  }
}
