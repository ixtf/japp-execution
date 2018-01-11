import {EvaluateField} from './evaluate-field';
import {LogableEntity} from './logable-entity';

export class EvaluateTemplate extends LogableEntity {
  name: string;
  fields: EvaluateField[];
}
