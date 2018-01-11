import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {Task} from '../../shared/models/task';
import {TaskGroup} from '../../shared/models/task-group';
import {TasksFollowInvite} from '../../shared/models/tasks-follow-invite';
import {TasksInvite} from '../../shared/models/tasks-invite';

@Injectable()
export class TaskBatchService {
  private baseUrl: string;

  constructor(private http: HttpClient, private store: Store<any>) {
    this.baseUrl = `${baseApiUrl}/taskBatch`;
  }

  listTaskGroup(): Observable<TaskGroup[]> {
    return this.http.get(`${this.baseUrl}/taskGroups`);
  }

  listTask(taskGroupId: string): Observable<Task[]> {
    return this.http.get(`${this.baseUrl}/taskGroups/${taskGroupId}/tasks`);
  }

  tasksInvite(tasks: Task[]): Observable<TasksInvite> {
    return this.http.post(`${this.baseUrl}/invite`, {tasks});
  }

  tasksFollowInvite(tasks: Task[]): Observable<TasksFollowInvite> {
    return this.http.post(`${this.baseUrl}/followInvite`, {tasks});
  }

  updateNickNames(tasks: Task[], nickNameMap: { [id: string]: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/nickNames`, {tasks, nickNameMap});
  }
}
