import {LogableEntity} from './logable-entity';
import {UploadFile} from './upload-file';

export class TaskGroup extends LogableEntity {
  name: string;
  logo: UploadFile;
  sign: UploadFile;
  signString: string;

  static toEntities(taskGroups, entities?: { [id: string]: TaskGroup }): { [id: string]: TaskGroup } {
    taskGroups = taskGroups || [];
    entities = entities || {};
    return taskGroups.reduce((acc, cur) => {
      acc[cur.id] = cur;
      return acc;
    }, {...entities});
  }
}

export const MyTaskGroup: TaskGroup = Object.assign(new TaskGroup(), {id: '0', name: '我的'});
