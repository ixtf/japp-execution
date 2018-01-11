import {channelManagePageActions} from '../actions';

export interface State {
  error: string | null;
  pending: boolean;
}

export const initialState: State = {
  error: null,
  pending: false,
};

export function reducer(state = initialState, action: channelManagePageActions.Actions): State {
  switch (action.type) {
    case channelManagePageActions.INIT_SUCCESS: {
      return {
        ...state,
        error: null,
        pending: false,
      };
    }
    default: {
      return state;
    }
  }
}

export const getError = (state: State) => state.error;
export const getPending = (state: State) => state.pending;
