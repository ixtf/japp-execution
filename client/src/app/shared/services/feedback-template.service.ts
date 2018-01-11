import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {CurdService} from '../../core/services/curd.service';
import {FeedbackTemplate} from '../models/feedback-template';

@Injectable()
export class FeedbackTemplateService extends CurdService<FeedbackTemplate, string> {

  constructor(http: HttpClient, private store: Store<any>) {
    super(http, 'feedbackTemplates');
  }

}
