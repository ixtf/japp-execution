import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {Channel} from '../../shared/models/channel';
import {Plan} from '../../shared/models/plan';
import {PlanInvite} from '../../shared/models/plan-invite';
import {Task} from '../../shared/models/task';
import {TaskComplain} from '../../shared/models/task-complain';
import {TaskGroup} from '../../shared/models/task-group';

@Injectable()
export class ApiService {
  constructor(private http: HttpClient) {
  }

  listTaskGroup(cond: {}): Observable<TaskGroup> {
    return this.http.get(``);
  }

  createTaskComplain(taskId: string, taskComplain: TaskComplain): Observable<TaskComplain> {
    return this.http.post<TaskComplain>(`${baseApiUrl}/taskComplains?taskId=${taskId}`, taskComplain);
  }

  updateTaskComplain(id: string, taskComplain: TaskComplain): Observable<TaskComplain> {
    return this.http.put<TaskComplain>(`${baseApiUrl}/taskComplains/${id}`, taskComplain);
  }

  savePlan(plan: Plan): Observable<Plan> {
    return plan.id ? this.updatePlan(plan.id, plan) : this.createPlan(plan);
  }

  createPlan(plan: Plan): Observable<Plan> {
    return this.http.post<Plan>(`${baseApiUrl}/plans`, plan);
  }

  updatePlan(id: string, plan: Plan): Observable<Plan> {
    return this.http.put<Plan>(`${baseApiUrl}/plans/${id}`, plan);
  }

  getPlan(id: string): Observable<Plan> {
    return this.http.get<Plan>(`${baseApiUrl}/plans/${id}`);
  }

  deletePlan(id: string): Observable<any> {
    return this.http.delete(`${baseApiUrl}/plans/${id}`);
  }

  listPlan(params: HttpParams): Observable<{ first: number, pageSize: number, count: number, plans: Plan[] }> {
    return this.http.get<{ first: number, pageSize: number, count: number, plans: Plan[] }>(`${baseApiUrl}/plans`, {params});
  }

  planInvite(id: string): Observable<PlanInvite> {
    return this.http.get<PlanInvite>(`${baseApiUrl}/plans/${id}/inviteTicket`);
  }

  listPlan_Admin(params: HttpParams): Observable<{ first: number, pageSize: number, count: number, plans: Plan[] }> {
    return this.http.get<{ first: number, pageSize: number, count: number, plans: Plan[] }>(`${baseApiUrl}/admin/plans`, {params});
  }

  auditPlan(id: string): Observable<any> {
    return this.http.put(`${baseApiUrl}/admin/plans/${id}/audit`, null);
  }

  unAuditPlan(id: string): Observable<any> {
    return this.http.delete(`${baseApiUrl}/admin/plans/${id}/audit`);
  }

  listChannel(): Observable<Channel[]> {
    return this.http.get<Channel[]>(`${baseApiUrl}/channels`);
  }

  listTask_Admin(params: HttpParams): Observable<{ tasks: Task[], count: number, first: number, pageSize: number }> {
    return this.http.get<{ tasks: Task[], count: number, first: number, pageSize: number }>(`${baseApiUrl}/admin/tasks`, {params});
  }

  listTaskComplain_Admin(taskId: string): Observable<{ task: Task, taskComplains: TaskComplain[] }> {
    const params = new HttpParams().set('taskId', taskId);
    return this.http.get<{ task: Task, taskComplains: TaskComplain[] }>(`${baseApiUrl}/admin/taskComplains`, {params});
  }
}
