import {createSelector} from '@ngrx/store';
import {Enlist} from '../../../shared/models/enlist';
import {enlistActions, enlistHistoryPageActions} from '../actions';

export interface State {
  entities: {};
  first: number;
  pageSize: number;
  count: number;
}

const initialState: State = {
  first: 0,
  pageSize: 50,
  count: 0,
  entities: {},
};

export function reducer(state = initialState, action: enlistActions.Actions | enlistHistoryPageActions.Actions): State {
  switch (action.type) {
    case enlistHistoryPageActions.LIST_SUCCESS: {
      const {first, pageSize, count, enlists} = action;
      const entities = Enlist.toEntities(enlists, {});
      return {...state, first, pageSize, count, entities};
    }

    case enlistActions.RESTART_SUCCESS:
    case enlistActions.DELETE_SUCCESS: {
      const entities = {...state.entities};
      delete entities[action.enlistId];
      return {...state, entities};
    }

    default:
      return state;
  }
}

export const getFirst = (state: State) => state.first;
export const getPageSiz = (state: State) => state.pageSize;
export const getCount = (state: State) => state.count;
export const getEntities = (state: State) => state.entities;
export const getEnlists = createSelector(getEntities, entities => Object.keys(entities).map(id => entities[id]));
