import {Operator} from '../../../shared/models/operator';
import {coreActions, loginPageActions} from '../actions';

export interface State {
  authOperator: Operator;
  isAdmin: boolean;
  isMobile: boolean;
  sidenavOpened: boolean;
  showSidenav: boolean;
  showToolbar: boolean;
}

const initialState: State = {
  authOperator: null,
  isAdmin: false,
  isMobile: false,
  sidenavOpened: false,
  showSidenav: false,
  showToolbar: false,
};

export function reducer(state = initialState, action: coreActions.Actions | loginPageActions.Actions): State {
  switch (action.type) {
    case loginPageActions.LOGIN_SUCCESS: {
      const {isAdmin, operator} = action.payload;
      return {...state, isAdmin, authOperator: operator,};
    }

    case coreActions.CLOSE_SIDENAV: {
      return {...state, sidenavOpened: false};
    }

    case coreActions.OPEN_SIDENAV: {
      return {...state, sidenavOpened: true};
    }

    case coreActions.SET_IS_MOBILE: {
      const isMobile = action.payload;
      return {...state, isMobile, sidenavOpened: !isMobile};
    }

    case coreActions.SET_SHOW_SIDENAV: {
      return {...state, showSidenav: action.payload};
    }

    case coreActions.SET_SHOW_TOOLBAR: {
      return {...state, showToolbar: action.payload};
    }

    default:
      return state;
  }
}

export const getAuthOperator = (state: State) => state.authOperator;
export const getIsAdmin = (state: State) => state.isAdmin;
export const getIsMobile = (state: State) => state.isMobile;
export const getSidenavOpened = (state: State) => state.sidenavOpened;
export const getShowSidenav = (state: State) => state.showSidenav;
export const getShowToolbar = (state: State) => state.showToolbar;
