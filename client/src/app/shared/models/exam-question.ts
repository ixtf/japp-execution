import {ExamQuestionLab} from './exam-question-lab';
import {LogableEntity} from './logable-entity';
import {UploadFile} from './upload-file';

export class ExamQuestion extends LogableEntity {
  lab: ExamQuestionLab;
  name: string;
  question: UploadFile;
  answer: UploadFile;
  tags: string[];
}
