import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import 'rxjs/add/operator/distinct';
import {CurdService} from '../../core/services/curd.service';
import {EvaluateTemplate} from '../models/evaluate-template';

@Injectable()
export class EvaluateTemplateService extends CurdService<EvaluateTemplate, string> {

  constructor(http: HttpClient, private store: Store<any>) {
    super(http, 'evaluateTemplates');
  }

}
