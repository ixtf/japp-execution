import {EvaluateFieldValue} from './evaluate-field-value';
import {LogableEntity} from './logable-entity';
import {Operator} from './operator';

export class TaskEvaluate extends LogableEntity {
  executor: Operator;
  note: string;
  fieldsValue: EvaluateFieldValue[];
}
