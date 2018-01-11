import {EvaluateTemplate} from './evaluate-template';
import {FeedbackTemplate} from './feedback-template';
import {LogableEntity} from './logable-entity';
import {Operator} from './operator';
import {Plan} from './plan';
import {TaskContextData} from './task-context-data';
import {TaskEvaluate} from './task-evaluate';
import {TaskFeedback} from './task-feedback';
import {MyTaskGroup} from './task-group';
import {TaskOperatorContextData} from './task-operator-context-data';
import {UploadFile} from './upload-file';

export class Task extends LogableEntity {
  taskGroup = MyTaskGroup;
  plan: Plan;
  title: string;
  content: string;
  status: 'RUN' | 'FINISH' = 'RUN';
  charger: Operator;
  startDate = new Date();
  endDate: Date;
  participants: Operator[];
  followers: Operator[];
  tags: string[];
  attachments: UploadFile[];
  feedbackTemplate: FeedbackTemplate;
  evaluateTemplate: EvaluateTemplate;
  taskFeedbacks: TaskFeedback[];
  taskEvaluates: TaskEvaluate[];
  taskContextData: TaskContextData;
  taskOperatorContextDatas: TaskOperatorContextData[];
  taskOperatorContextData: TaskOperatorContextData;

  get participantsCount(): number {
    return this.participants ? this.participants.length : 0;
  }

  get followersCount(): number {
    return this.followers ? this.followers.length : 0;
  }

  get attachmentsCount(): number {
    return this.attachments ? this.attachments.length : 0;
  }

  get taskFeedbacksCount(): number {
    return this.taskFeedbacks ? this.taskFeedbacks.length : 0;
  }

  get taskEvaluatesCount(): number {
    return this.taskEvaluates ? this.taskEvaluates.length : 0;
  }

  get isStarted(): boolean {
    return !moment().isBefore(moment(this.startDate));
  }

  static assign(...sources: any[]): Task {
    const task = Object.assign(new Task(), ...sources);
    task.taskGroup = task.taskGroup || MyTaskGroup;
    return task;
  }

  static toEntities(tasks: Task[], entities?: { [id: string]: Task }): { [id: string]: Task } {
    tasks = tasks || [];
    entities = entities || {};
    return tasks.reduce((acc, cur) => {
      acc[cur.id] = Task.assign(cur);
      return acc;
    }, {...entities});
  }
}
