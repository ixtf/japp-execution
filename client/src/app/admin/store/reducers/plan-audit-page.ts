import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {Plan} from '../../../shared/models/plan';
import {planAuditPageActions} from '../actions';

export interface State {
  planEntities: { [id: string]: Plan };
}

export const initialState: State = {
  planEntities: {},
};

export function reducer(state = initialState, action: planAuditPageActions.Actions): State {
  switch (action.type) {
    case planAuditPageActions.INIT_SUCCESS: {
      const {plans} = action.payload;
      const planEntities = Plan.toEntities(plans);
      return {...state, planEntities};
    }

    case planAuditPageActions.AUDIT_SUCCESS:
    case planAuditPageActions.UNAUDIT_SUCCESS: {
      const {plan} = action.payload;
      const planEntities = {...state.planEntities};
      delete planEntities[plan.id];
      return {...state, planEntities};
    }

    default: {
      return state;
    }
  }
}

export const getPlanEntities = (state: State) => state.planEntities;
export const getPlans = createSelector(getPlanEntities, entities => Object.keys(entities).map(it => entities[it]).sort(DefaultCompare));
