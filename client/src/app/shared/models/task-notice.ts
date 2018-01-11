import {Entity} from './entity';
import {Operator} from './operator';

export class TaskNotice extends Entity {
  noticeDateTime = new Date();
  noticeDate ? = new Date();
  noticeTime ? = moment([2017, 0, 1, 8, 0, 0, 0]).toDate();
  receivers: Operator[];
  content: string;
}
