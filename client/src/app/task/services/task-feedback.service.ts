import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {TaskFeedback} from '../../shared/models/task-feedback';

@Injectable()
export class TaskFeedbackService {
  private baseTaskUrl = `${baseApiUrl}/tasks`;
  private baseUrl = `${baseApiUrl}/taskFeedbacks`;

  constructor(private http: HttpClient, private store: Store<any>) {
  }

  save(taskId: string, taskFeedback: TaskFeedback): Observable<TaskFeedback> {
    return taskFeedback.id ? this.update(taskId, taskFeedback) : this.create(taskId, taskFeedback);
  }

  create(taskId: string, taskFeedback: TaskFeedback): Observable<TaskFeedback> {
    return this.http.post(`${this.baseTaskUrl}/${taskId}/taskFeedbacks`, taskFeedback);
  }

  update(taskId: string, taskFeedback: TaskFeedback): Observable<TaskFeedback> {
    return this.http.put(`${this.baseUrl}/${taskFeedback.id}`, taskFeedback);
  }

  list(taskId: string): Observable<TaskFeedback[]> {
    return this.http.get(`${this.baseTaskUrl}/${taskId}/taskFeedbacks`);
  }

  delete(taskFeedbackId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${taskFeedbackId}`);
  }

}
