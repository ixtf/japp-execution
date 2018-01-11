import {Field} from './field';
import {LogableEntity} from './logable-entity';

export class FeedbackTemplate extends LogableEntity {
  name: string;
  fields: Field[];
}
