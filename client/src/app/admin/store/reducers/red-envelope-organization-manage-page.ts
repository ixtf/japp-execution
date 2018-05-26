import {createSelector} from '@ngrx/store';
import {RedEnvelopeOrganization} from '../../../shared/models/red-envelope-organization';
import {redEnvelopeOrganizationManagePageActions} from '../actions';

export interface State {
  redEnvelopeOrganizationEntities: { [id: string]: RedEnvelopeOrganization };
}

export const initialState: State = {
  redEnvelopeOrganizationEntities: {},
};

export function reducer(state = initialState, action: redEnvelopeOrganizationManagePageActions.Actions): State {
  switch (action.type) {
    case redEnvelopeOrganizationManagePageActions.INIT_SUCCESS: {
      const redEnvelopeOrganizationEntities = (action.redEnvelopeOrganizations || []).reduce((acc, cur) => {
        acc[cur.id] = cur;
        return acc;
      }, {});
      return {
        ...state,
        redEnvelopeOrganizationEntities,
      };
    }

    default: {
      return state;
    }
  }
}

export const getRedEnvelopeOrganizationEntities = (state: State) => state.redEnvelopeOrganizationEntities;
export const getRedEnvelopeOrganizations = createSelector(getRedEnvelopeOrganizationEntities, entities => Object.keys(entities).map(it => entities[it]));
