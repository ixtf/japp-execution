import {LogableEntity} from './logable-entity';
import {Task} from './task';

export class TaskComplain extends LogableEntity {
  task: Task;
  content: string;

  static assign(...sources: any[]): TaskComplain {
    const result = Object.assign(new TaskComplain(), ...sources);
    return result;
  }

  static toEntities(os: TaskComplain[], entities?: { [id: string]: TaskComplain }): { [id: string]: TaskComplain } {
    return (os || []).reduce((acc, cur) => {
      acc[cur.id] = TaskComplain.assign(cur);
      return acc;
    }, {...(entities || {})});
  }
}
