import {LogableEntity} from './logable-entity';
import {Operator} from './operator';

export class ExamQuestionLab extends LogableEntity {
  name: string;
  participants: Operator[];
}
