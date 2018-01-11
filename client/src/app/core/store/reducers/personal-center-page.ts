import {Operator} from '../../../shared/models/operator';
import {personalCenterPageActions} from '../actions';

export interface State {
  operator: Operator;
}

const initialState: State = {
  operator: null,
};

export function reducer(state = initialState, action: personalCenterPageActions.Actions): State {
  switch (action.type) {
    case personalCenterPageActions.INIT_SUCCESS: {
      const {operator} = action;
      return {...state, operator};
    }

    default:
      return state;
  }
}

export const getOperator = (state: State) => state.operator;
