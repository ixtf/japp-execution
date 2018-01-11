import {EnlistFeedback} from './enlist-feedback';
import {FeedbackTemplate} from './feedback-template';
import {LogableEntity} from './logable-entity';
import {Operator} from './operator';
import {PaymentMerchant} from './payment-merchant';
import {UploadFile} from './upload-file';

export class Enlist extends LogableEntity {
  paymentMerchant: PaymentMerchant;
  title: string;
  content: string;
  money: number;
  feedbackLimit = 0;
  status: 'RUN' | 'FINISH' = 'RUN';
  startDate = new Date();
  endDate: Date;
  tags: string[];
  attachments: UploadFile[];
  feedbackTemplate: FeedbackTemplate;
  enlistFeedbacks: EnlistFeedback[];

  get feedbacksCount(): number {
    return this.enlistFeedbacks && this.enlistFeedbacks.length || 0;
  }

  get attachmentsCount(): number {
    return this.attachments && this.attachments.length || 0;
  }

  get managersCount(): number {
    return this.paymentMerchant && this.paymentMerchant.managers && this.paymentMerchant.managers.length || 0;
  }

  static assign(...sources: any[]): Enlist {
    const enlist = Object.assign(new Enlist(), ...sources);
    enlist.startDate = enlist.startDate || new Date();
    enlist.paymentMerchant = Object.assign(new PaymentMerchant(), enlist.paymentMerchant);
    return enlist;
  }

  static toEntities(enlists: Enlist[], entities?: { [id: string]: Enlist }) {
    enlists = enlists || [];
    entities = entities || {};
    return enlists.reduce((acc, cur) => {
      acc[cur.id] = Enlist.assign(cur);
      return acc;
    }, {...entities});
  }

  isManager(operator: Operator): boolean {
    return this.paymentMerchant.isManager(operator);
  }
}
