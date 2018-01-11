import {Entity} from './entity';
import {Operator} from './operator';

export abstract class LogableEntity extends Entity {
  creator: Operator;
  createDateTime: Date;
  modifier: Operator;
  modifyDateTime: Date;
}
