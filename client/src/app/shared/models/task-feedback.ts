import {FieldValue} from './field-value';
import {LogableEntity} from './logable-entity';
import {TaskFeedbackComment} from './task-feedback-comment';
import {UploadFile} from './upload-file';

export class TaskFeedback extends LogableEntity {
  note: string;
  attachments: UploadFile[];
  fieldsValue: FieldValue[];
  taskFeedbackComments: TaskFeedbackComment[];
}
