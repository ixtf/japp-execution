import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {TaskNotice} from '../../shared/models/task-notice';

@Injectable()
export class TaskNoticeService {
  private baseTaskUrl = `${baseApiUrl}/tasks`;
  private baseUrl = `${baseApiUrl}/taskNotices`;

  constructor(private http: HttpClient, private store: Store<any>) {
  }

  save(taskId: string, taskNotice: TaskNotice): Observable<TaskNotice> {
    return taskNotice.id ? this.update(taskId, taskNotice) : this.create(taskId, taskNotice);
  }

  create(taskId: string, taskNotice: TaskNotice): Observable<TaskNotice> {
    return this.http.post(`${this.baseTaskUrl}/${taskId}/taskNotices`, taskNotice);
  }

  update(taskId: string, taskNotice: TaskNotice): Observable<TaskNotice> {
    return this.http.put(`${this.baseUrl}/${taskNotice.id}`, taskNotice);
  }

  list(taskId: string): Observable<TaskNotice[]> {
    return this.http.get(`${this.baseTaskUrl}/${taskId}/taskNotices`);
  }

  delete(id: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

}
