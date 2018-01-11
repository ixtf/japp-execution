import {createSelector} from '@ngrx/store';
import {DefaultCompare} from '../../../core/services/util.service';
import {Enlist} from '../../../shared/models/enlist';
import {enlistActions, enlistFeedbackActions, enlistProgressPageActions} from '../actions';

export interface State {
  enlistEntities: { [id: string]: Enlist };
  enlistId: string | null;
  enlistFilter: (enlist: Enlist) => boolean;
  enlistSort: (o1: Enlist, o2: Enlist) => number;
  rightTabIndex: number;
}

const initialState: State = {
  enlistEntities: {},
  enlistId: null,
  enlistFilter: null,
  enlistSort: DefaultCompare,
  rightTabIndex: 1,
};

export function reducer(state = initialState, action: enlistFeedbackActions.Actions | enlistActions.Actions | enlistProgressPageActions.Actions): State {
  switch (action.type) {
    case enlistProgressPageActions.INIT_SUCCESS: {
      const {enlists, enlistId} = action;
      const enlistEntities = Enlist.toEntities(enlists, state.enlistEntities);
      return {...state, enlistEntities, enlistId};
    }

    case enlistActions.GET_SUCCESS: {
      const enlist = Enlist.assign(state.enlistEntities[action.enlist.id], action.enlist);
      const enlistEntities = {...state.enlistEntities};
      enlistEntities[enlist.id] = enlist;
      return {...state, enlistEntities};
    }
    case enlistActions.DELETE_SUCCESS:
    case enlistActions.DONE_SUCCESS: {
      const enlistEntities = {...state.enlistEntities};
      delete enlistEntities[action.enlistId];
      return {...state, enlistEntities};
    }

    case enlistFeedbackActions.LIST_SUCCESS: {
      const {enlistId, enlistFeedbacks} = action;
      const enlist = Enlist.assign({id: enlistId}, state.enlistEntities[enlistId], {
        enlistFeedbacks: (enlistFeedbacks || []).sort((o1, o2) => {
          const b1 = o1 && o1.payment && o1.payment.successed || false;
          const b2 = o2 && o2.payment && o2.payment.successed || false;
          if (b1 === b2) {
            return DefaultCompare(o1, o2);
          } else if (!b1) {
            return -1;
          } else {
            return 1;
          }
        })
      });
      const enlistEntities = {...state.enlistEntities};
      enlistEntities[enlistId] = enlist;
      return {...state, enlistEntities};
    }
    case enlistProgressPageActions.SET_RIGHT_TAB_INDEX: {
      return {
        ...state,
        rightTabIndex: action.payload
      };
    }

    default:
      return state;
  }
}

export const getEnlistEntities = (state: State) => state.enlistEntities;
export const getEnlistId = (state: State) => state.enlistId;
export const getEnlistSort = (state: State) => state.enlistSort || DefaultCompare;
export const getEnlistFilter = (state: State) => state.enlistFilter;
export const getRightTabIndex = (state: State) => state.rightTabIndex;
export const getEnlists = createSelector(getEnlistEntities, getEnlistFilter, getEnlistSort, (entities, filter, sort) => {
  return Object.keys(entities)
    .map(id => entities[id])
    .filter(it => filter ? filter(it) : true)
    .sort((o1, o2) => sort(o1, o2));
});
export const getEnlist = createSelector(getEnlistEntities, getEnlistId, (entities, id) => id ? entities[id] : null);
export const getEnlistFeedbacks = createSelector(getEnlist, getEnlistSort, (enlist, sort) => {
  return (enlist && enlist.enlistFeedbacks || []).sort(DefaultCompare);
});
