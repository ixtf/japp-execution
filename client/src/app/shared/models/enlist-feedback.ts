import {EnlistFeedbackPayment} from './enlist-feedback-payment';
import {FieldValue} from './field-value';
import {LogableEntity} from './logable-entity';
import {UploadFile} from './upload-file';

export class EnlistFeedback extends LogableEntity {
  payment: EnlistFeedbackPayment;
  note: string;
  attachments: UploadFile[];
  fieldsValue: FieldValue[];
}
