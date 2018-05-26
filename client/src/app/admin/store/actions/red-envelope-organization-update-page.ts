import {Action} from '@ngrx/store';
import {RedEnvelopeOrganization} from '../../../shared/models/red-envelope-organization';

export const INIT_SUCCESS = '[AdminRedEnvelopeOrganizationUpdatePage] INIT_SUCCESS';

export class InitSuccess implements Action {
  readonly type = INIT_SUCCESS;

  constructor(public redEnvelopeOrganization: RedEnvelopeOrganization) {
  }
}

export type Actions
  = InitSuccess;
