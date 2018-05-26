import {RedEnvelopeOrganization} from '../../../shared/models/red-envelope-organization';
import {redEnvelopeOrganizationUpdatePageActions} from '../actions';

export interface State {
  redEnvelopeOrganization: RedEnvelopeOrganization;
}

export const initialState: State = {
  redEnvelopeOrganization: null,
};

export function reducer(state = initialState, action: redEnvelopeOrganizationUpdatePageActions.Actions): State {
  switch (action.type) {
    case redEnvelopeOrganizationUpdatePageActions.INIT_SUCCESS: {
      const {redEnvelopeOrganization} = action;
      return {...state, redEnvelopeOrganization};
    }

    default: {
      return state;
    }
  }
}

export const getRedEnvelopeOrganization = (state: State) => state.redEnvelopeOrganization;
