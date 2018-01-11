import {Plan} from '../../../shared/models/plan';
import {planUpdatePageActions} from '../actions';

export interface State {
  plan: Plan;
}

const initialState: State = {
  plan: new Plan(),
};

export function reducer(state = initialState, action: planUpdatePageActions.Actions): State {
  switch (action.type) {
    case planUpdatePageActions.INIT_SUCCESS: {
      return {
        ...state,
        plan: action.plan,
      };
    }

    default:
      return state;
  }
}

export const getPlan = (state: State) => state.plan;
