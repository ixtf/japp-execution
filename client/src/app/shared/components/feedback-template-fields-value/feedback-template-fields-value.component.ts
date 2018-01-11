import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {FeedbackTemplate} from '../../../shared/models/feedback-template';
import {Field} from '../../models/field';
import {FieldValue} from '../../models/field-value';

@Component({
  selector: 'jwjh-feedback-template-fields-value',
  templateUrl: './feedback-template-fields-value.component.html',
  styleUrls: ['./feedback-template-fields-value.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FeedbackTemplateFieldsValueComponent {
  @Input()
  feedbackTemplate: FeedbackTemplate;
  @Input()
  fieldsValue: FieldValue[];

  constructor(private translate: TranslateService) {
  }

  getFieldValue(field: Field): Observable<string | any> {
    const fieldValue = (this.fieldsValue || []).find(it => it.id === field.id);
    switch (field.type) {
      case 'SELECT_MULTIPLE': {
        return of(fieldValue && fieldValue.valuesString || []);
      }
      case 'BOOLEAN': {
        const b = fieldValue && fieldValue.valueString;
        if (b === 'true') {
          return this.translate.get('COMMON.YES');
        }
        return this.translate.get('COMMON.NO');
      }
      default:
        return of(fieldValue && fieldValue.valueString);
    }
  }
}
