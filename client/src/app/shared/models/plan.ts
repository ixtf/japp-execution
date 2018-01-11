import {Channel} from './channel';
import {LogableEntity} from './logable-entity';
import {Operator} from './operator';
import {PlanItem} from './plan-item';

export class Plan extends LogableEntity {
  channel: Channel;
  name: string;
  adUrl: string;
  note: string;
  tags: string[];
  publisher: Operator;
  publishDate: Date;
  published: boolean;
  audited: boolean;
  shared: boolean;
  items: PlanItem[];

  static assign(...sources: any[]): Plan {
    const result = Object.assign(new Plan(), ...sources);
    return result;
  }

  static toEntities(os: Plan[], entities?: { [id: string]: Plan }): { [id: string]: Plan } {
    return (os || []).reduce((acc, cur) => {
      acc[cur.id] = Plan.assign(cur);
      return acc;
    }, {...(entities || {})});
  }
}
