import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {TaskEvaluate} from '../../shared/models/task-evaluate';

@Injectable()
export class TaskEvaluateService {
  private baseTaskUrl = `${baseApiUrl}/tasks`;
  private baseUrl = `${baseApiUrl}/taskEvaluates`;

  constructor(private http: HttpClient, private store: Store<any>) {
  }

  save(taskId: string, taskEvaluate: TaskEvaluate): Observable<TaskEvaluate> {
    return taskEvaluate.id ? this.update(taskId, taskEvaluate) : this.create(taskId, taskEvaluate);
  }

  create(taskId: string, taskEvaluate: TaskEvaluate): Observable<TaskEvaluate> {
    return this.http.post(`${this.baseTaskUrl}/${taskId}/taskEvaluates`, taskEvaluate);
  }

  update(taskId: string, taskEvaluate: TaskEvaluate): Observable<TaskEvaluate> {
    return this.http.put(`${this.baseUrl}/${taskEvaluate.id}`, taskEvaluate);
  }

  list(taskId: string): Observable<TaskEvaluate[]> {
    return this.http.get(`${this.baseTaskUrl}/${taskId}/taskEvaluates`);
  }

  delete(id: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

}
