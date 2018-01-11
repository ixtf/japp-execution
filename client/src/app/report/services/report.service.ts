import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';

@Injectable()
export class ReportService {
  protected baseUrl = `${baseApiUrl}/reports`;

  constructor(private http: HttpClient) {
  }

  fetchMyReport(): Observable<any> {
    return this.http.post(`${this.baseUrl}/my`, null);
  }
}
