import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {TaskFeedbackComment} from '../../shared/models/task-feedback-comment';

@Injectable()
export class TaskFeedbackCommentService {
  private baseTaskFeedbackUrl = `${baseApiUrl}/taskFeedbacks`;
  private baseUrl = `${baseApiUrl}/taskFeedbackComments`;

  constructor(private http: HttpClient, private store: Store<any>) {
  }

  save(taskFeedbackId: string, taskFeedbackComment: TaskFeedbackComment): Observable<TaskFeedbackComment> {
    return taskFeedbackComment.id ? this.update(taskFeedbackId, taskFeedbackComment) : this.create(taskFeedbackId, taskFeedbackComment);
  }

  create(taskFeedbackId: string, taskFeedbackComment: TaskFeedbackComment): Observable<TaskFeedbackComment> {
    return this.http.post(`${this.baseTaskFeedbackUrl}/${taskFeedbackId}/taskFeedbackComments`, taskFeedbackComment);
  }

  update(taskFeedbackId: string, taskFeedbackComment: TaskFeedbackComment): Observable<TaskFeedbackComment> {
    return this.http.put(`${this.baseUrl}/${taskFeedbackComment.id}`, taskFeedbackComment);
  }

  list(taskFeedbackId: string): Observable<TaskFeedbackComment[]> {
    return this.http.get(`${this.baseTaskFeedbackUrl}/${taskFeedbackId}/taskFeedbackComments`);
  }

  delete(taskFeedbackCommentId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${taskFeedbackCommentId}`);
  }

}
