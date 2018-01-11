import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {CurdService} from '../../core/services/curd.service';
import {Task} from '../../shared/models/task';
import {MyTaskGroup, TaskGroup} from '../../shared/models/task-group';

@Injectable()
export class TaskGroupService extends CurdService<TaskGroup, string> {

  constructor(http: HttpClient) {
    super(http, 'taskGroups');
  }

  get(id: string): Observable<TaskGroup> {
    if (id && id !== MyTaskGroup.id) {
      return super.get(id);
    }
    return of(MyTaskGroup);
  }

  update(taskGroup: TaskGroup): Observable<TaskGroup> {
    if (taskGroup.id === MyTaskGroup.id) {
      return of(MyTaskGroup);
    }
    return super.update(taskGroup);
  }

  listTask(id: string): Observable<Task[]> {
    return this.http.get(`${this.baseUrl}/${id}/tasks`);
  }

  top(id: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/top`, null);
  }
}
