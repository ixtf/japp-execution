import {HttpClient} from '@angular/common/http';
import {Pipe, PipeTransform} from '@angular/core';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {baseApiUrl} from '../../../environments/environment';
import {Operator} from '../models/operator';
import {Task} from '../models/task';
import {TaskOperatorContextData} from '../models/task-operator-context-data';

const nicknameMap: { [key: string]: BehaviorSubject<string> } = {};

@Pipe({
  name: 'operatorNickname'
})
export class OperatorNicknamePipe implements PipeTransform {

  constructor(private http: HttpClient) {
  }

  static key(operator: Operator, task: Task): string {
    return `operatorNickname-${task.id}-${operator.id}`;
  }

  static get(operator: Operator, task: Task): string {
    const key = OperatorNicknamePipe.key(operator, task);
    return sessionStorage.getItem(key) || operator.name;
  }

  transform(operator: Operator, task: Task): Observable<string> {
    if (!operator || !task) {
      return of('');
    }

    const key = OperatorNicknamePipe.key(operator, task);
    const nickName = sessionStorage.getItem(key);
    if (nickName) {
      return of(nickName);
    }

    let nickName$ = nicknameMap[key];
    if (!nickName$) {
      nickName$ = new BehaviorSubject<string>(operator.name);
      nicknameMap[key] = nickName$;
      this.getOperatorContextData(task.id, operator.id)
        .subscribe(res => {
          sessionStorage.setItem(key, res.nickName || operator.name);
          delete nicknameMap[key];
          if (res.nickName) {
            nickName$.next(res.nickName);
          }
        });
    }
    return nickName$;
  }

  getOperatorContextData(taskId: string, operatorId: string): Observable<TaskOperatorContextData> {
    return this.http.get(`${baseApiUrl}/tasks/${taskId}/operators/${operatorId}/taskOperatorContextData`);
  }

}
