import {Entity} from './entity';
import {Operator} from './operator';

export class PaymentMerchant extends Entity {
  name: string;
  sub_mch_id: string;
  sub_appid?: string;
  managers: Operator[];

  static assign(...sources: any[]): PaymentMerchant {
    const result = Object.assign(new PaymentMerchant(), ...sources);
    return result;
  }

  static toEntities(os: PaymentMerchant[], entities?: { [id: string]: PaymentMerchant }): { [id: string]: PaymentMerchant } {
    return (os || []).reduce((acc, cur) => {
      acc[cur.id] = PaymentMerchant.assign(cur);
      return acc;
    }, {...(entities || {})});
  }

  isManager(operator: Operator): boolean {
    const find = (this.managers || []).find(it => it.id === operator.id);
    return !!find;
  }
}
