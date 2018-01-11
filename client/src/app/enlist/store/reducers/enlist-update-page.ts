import {createSelector} from '@ngrx/store';
import {Enlist} from '../../../shared/models/enlist';
import {enlistUpdatePageActions} from '../actions';

export interface State {
  enlist: Enlist;
}

const initialState: State = {
  enlist: new Enlist(),
};

export function reducer(state = initialState, action: enlistUpdatePageActions.Actions): State {
  switch (action.type) {
    case enlistUpdatePageActions.INIT_SUCCESS: {
      return {...state, enlist: action.enlist};
    }

    default:
      return state;
  }
}

export const getEnlist = (state: State) => state.enlist;
export const getTitle = createSelector(getEnlist, it => it.id ? 'UPDATE' : 'CREATE');

