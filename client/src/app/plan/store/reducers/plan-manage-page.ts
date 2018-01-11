import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {Plan} from '../../../shared/models/plan';
import {planManagePageActions} from '../actions';

export interface State {
  draft: boolean;
  audited: boolean;
  published: boolean;
  planEntities: { [id: string]: Plan };
}

const initialState: State = {
  draft: true,
  audited: false,
  published: false,
  planEntities: {},
};

export function reducer(state = initialState, action: planManagePageActions.Actions): State {
  switch (action.type) {
    case planManagePageActions.INIT_SUCCESS: {
      const {plans, draft, audited, published} = action.payload;
      const planEntities = Plan.toEntities(plans);
      return {...state, draft, audited, published, planEntities};
    }

    default:
      return state;
  }
}

export const getPlanEntities = (state: State) => state.planEntities;
export const getAudited = (state: State) => state.audited;
export const getPublished = (state: State) => state.published;
export const getDraft = (state: State) => state.draft;
export const getPlans = createSelector(getPlanEntities, entities => Object.keys(entities).map(it => entities[it]).sort(DefaultCompare));
