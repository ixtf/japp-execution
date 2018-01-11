import {HttpClient, HttpParams} from '@angular/common/http';
import 'rxjs/add/operator/map';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {Entity} from '../../shared/models/entity';

export abstract class CurdService<T extends Entity, K> {
  protected baseUrl: string;

  constructor(protected http: HttpClient, private pathSuffix: string) {
    this.baseUrl = `${baseApiUrl}/${this.pathSuffix}`;
  }

  save(obj: T): Observable<T> {
    return obj.id ? this.update(obj) : this.create(obj);
  }

  create(obj: T): Observable<T> {
    return this.http.post(`${this.baseUrl}`, obj);
  }

  update(obj: T): Observable<T> {
    return this.http.put(`${this.baseUrl}/${obj.id}`, obj);
  }

  get(id: K): Observable<T> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  list(params?: HttpParams): Observable<T[]> | Observable<any> {
    return this.http.get(this.baseUrl, {params});
  }

  delete(id: any): Observable<Object> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
