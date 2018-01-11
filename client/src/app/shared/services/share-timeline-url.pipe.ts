import {HttpClient} from '@angular/common/http';
import {Pipe, PipeTransform} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl, shareTimelineUrls} from '../../../environments/environment';
import {Task} from '../models/task';
import {TaskGroup} from '../models/task-group';

const tokenMap: { [key: string]: Promise<string> } = {};

function tokenPromise(o: Task | TaskGroup, key: string, tokenObservable: Observable<{ token: string }>): Promise<string> {
  let token$: Promise<string>;
  const token = localStorage.getItem(key);
  if (token) {
    return Promise.resolve(token);
  }
  token$ = tokenMap[key];
  if (token$) {
    return token$;
  }
  token$ = tokenObservable
    .toPromise()
    .then(res => {
      localStorage.setItem(key, res.token);
      delete tokenMap[key];
      return res.token;
    });
  tokenMap[key] = token$;
  return token$;
}

@Pipe({
  name: 'shareTimelineUrl'
})
export class ShareTimelineUrlPipe implements PipeTransform {

  constructor(private http: HttpClient) {
  }

  static taskTokenKey(task: Task): string {
    return `shareTimelineToken-Task-${task.id}`;
  }

  static taskGroupTokenKey(taskGroup: TaskGroup): string {
    return `shareTimelineToken-TaskGroup-${taskGroup.id}`;
  }

  transform(o: Task | TaskGroup, type: 'Task' | 'TaskClock' | 'TaskGroup' = 'Task'): Promise<string> {
    if (!o) {
      return Promise.resolve('');
    }
    switch (type) {
      case 'Task': {
        const task: Task = o as Task;
        return tokenPromise(o, ShareTimelineUrlPipe.taskTokenKey(task), this.getTaskToken(task.id))
          .then(it => `${shareTimelineUrls.task}${it}`);
      }
      case 'TaskClock': {
        const task: Task = o as Task;
        return tokenPromise(o, ShareTimelineUrlPipe.taskTokenKey(task), this.getTaskToken(task.id))
          .then(it => `${shareTimelineUrls.taskClock}${it}`);
      }
      case 'TaskGroup': {
        const taskGroup: TaskGroup = o as TaskGroup;
        return tokenPromise(o, ShareTimelineUrlPipe.taskGroupTokenKey(taskGroup), this.getTaskGroupToken(taskGroup.id))
          .then(it => `${shareTimelineUrls.taskClock}${it}`);
      }
      default: {
        return Promise.resolve('');
      }
    }
  }

  getTaskToken(id: string): Observable<{ token: string }> {
    return this.http.get(`${baseApiUrl}/tasks/${id}/weixinShareTimelineToken`);
  }

  getTaskGroupToken(id: string): Observable<{ token: string }> {
    return this.http.get(`${baseApiUrl}/taskGroups/${id}/weixinShareTimelineToken`);
  }
}
