import {Entity} from './entity';
import {EvaluateTemplate} from './evaluate-template';
import {FeedbackTemplate} from './feedback-template';
import {UploadFile} from './upload-file';

export class PlanItem extends Entity {
  title: string;
  content: string;
  tags: string[];
  attachments: UploadFile[];
  feedbackTemplate: FeedbackTemplate;
  evaluateTemplate: EvaluateTemplate;
  sortBy: number;
}
