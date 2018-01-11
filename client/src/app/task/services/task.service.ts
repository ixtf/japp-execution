import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {baseApiUrl} from '../../../environments/environment';
import {CurdService} from '../../core/services/curd.service';
import {coreAuthOperator} from '../../core/store/index';
import {Operator} from '../../shared/models/operator';
import {Task} from '../../shared/models/task';
import {TaskContextData} from '../../shared/models/task-context-data';
import {TaskOperatorContextData} from '../../shared/models/task-operator-context-data';

export const isManager = (task: Task, operator: Operator): boolean => {
  const operatorId = operator.id;
  if (operatorId === (task.charger && task.charger.id)) {
    return true;
  }
  return !!(task.followers || []).find(it => it.id === operatorId);
};
export const isParticipant = (task: Task, operator: Operator): boolean => {
  const participants = task.participants || [];
  return !!participants.find(it => it.id === operator.id);
};

@Injectable()
export class TaskService extends CurdService<Task, string> {

  constructor(http: HttpClient, private store: Store<any>) {
    super(http, 'tasks');
  }

  isFinish(task: Task): boolean {
    return task.status === 'FINISH';
  }

  isManager(task: Task, o?: Operator): Observable<boolean> {
    if (!task) {
      return of(false);
    }
    const operator$ = o ? of(o) : this.store.select(coreAuthOperator).take(1);
    return operator$.map(operator => isManager(task, operator));
  }

  isParticipant(task: Task, o?: Operator): Observable<boolean> {
    if (!task) {
      return of(false);
    }
    const operator$ = o ? of(o) : this.store.select(coreAuthOperator).take(1);
    return operator$.map(operator => isParticipant(task, operator));
  }

  read(taskId: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${taskId}/read`, null);
  }

  top(taskId: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${taskId}/top`, null);
  }

  copy(taskId: string): Observable<Task> {
    return this.http.put(`${this.baseUrl}/${taskId}/copy`, null);
  }

  done(taskId: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${taskId}/finish`, null);
  }

  restart(taskId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${taskId}/finish`);
  }

  getContextData(taskId: string): Observable<TaskContextData> {
    return this.http.get(`${this.baseUrl}/${taskId}/contextData`);
  }

  getOperatorContextData(taskId: string, operatorId: string): Observable<TaskOperatorContextData> {
    return this.http.get(`${this.baseUrl}/${taskId}/operators/${operatorId}/taskOperatorContextData`);
  }

  listHistory(first: number, pageSize: number): Observable<{ count: number; tasks: Task[] }> {
    return this.http.get(`${baseApiUrl}/taskHistory?first=${first}&pageSize=${pageSize}`);
  }

  importParticipants(taskId: string, operators: Operator[]): Observable<any> {
    return this.http.put(`${this.baseUrl}/${taskId}/participants`, {operators});
  }

  deleteParticipant(taskId: string, participantId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${taskId}/participants/${participantId}`);
  }

  importFollowers(taskId: string, operators: Operator[]): Observable<any> {
    return this.http.put(`${this.baseUrl}/${taskId}/followers`, {operators});
  }

  deleteFollower(taskId: string, followerId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${taskId}/followers/${followerId}`);
  }
}
