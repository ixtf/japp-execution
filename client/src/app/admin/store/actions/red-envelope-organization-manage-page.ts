import {Action} from '@ngrx/store';
import {RedEnvelopeOrganization} from '../../../shared/models/red-envelope-organization';

export const INIT = '[AdminRedEnvelopeOrganizationManagePage] INIT';

export class Init implements Action {
  readonly type = INIT;
}

export const INIT_SUCCESS = '[AdminRedEnvelopeOrganizationManagePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public redEnvelopeOrganizations: RedEnvelopeOrganization[]) {
  }
}

export const INVITE = '[AdminRedEnvelopeOrganizationManagePage] INVITE';

export class Invite implements Action {
  readonly type = INVITE;

  constructor(public redEnvelopeOrganizationId: string) {
  }
}

export type Actions
  = Init
  | InitSuccess
  | Invite;
