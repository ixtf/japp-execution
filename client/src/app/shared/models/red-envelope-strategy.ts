import {Entity} from './entity';
import {RedEnvelopeOrganization} from './red-envelope-organization';

export class RedEnvelopeStrategy extends Entity {
  redEnvelopeOrganization: RedEnvelopeOrganization;
  count: number;
  min: number;
  max: number;
  expireDate: Date;
}
