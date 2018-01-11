import {ExamQuestion} from './exam-question';
import {LogableEntity} from './logable-entity';
import {UploadFile} from './upload-file';

export class TaskFeedbackComment extends LogableEntity {
  note: string;
  attachments: UploadFile[];
  examQuestions: ExamQuestion[];
}
