import {Entity} from './entity';
import {Operator} from './operator';

export class RedEnvelopeOrganization extends Entity {
  name: string;
  managers: Operator[];

  static assign(...sources: any[]): RedEnvelopeOrganization {
    const result = Object.assign(new RedEnvelopeOrganization(), ...sources);
    return result;
  }

  static toEntities(os: RedEnvelopeOrganization[], entities?: { [id: string]: RedEnvelopeOrganization }): { [id: string]: RedEnvelopeOrganization } {
    return (os || []).reduce((acc, cur) => {
      acc[cur.id] = RedEnvelopeOrganization.assign(cur);
      return acc;
    }, {...(entities || {})});
  }

  isManager(operator: Operator): boolean {
    const find = (this.managers || []).find(it => it.id === operator.id);
    return !!find;
  }
}
