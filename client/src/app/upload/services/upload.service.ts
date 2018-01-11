import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {baseApiUrl} from '../../../environments/environment';
import {UploadFile} from '../../shared/models/upload-file';

@Injectable()
export class UploadService {

  constructor(private http: HttpClient) {
  }

  updateFileName(id: string, name: string): Observable<UploadFile> {
    return this.http.put(`${baseApiUrl}/uploads/${id}/fileName`, {data: name});
  }

  getFileHtml(token: string): Observable<any> {
    return this.http.get(`${baseApiUrl}/opens/downloads/${token}/html`);
  }

}
