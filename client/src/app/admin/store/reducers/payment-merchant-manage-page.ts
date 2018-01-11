import {createSelector} from '@ngrx/store';
import {PaymentMerchant} from '../../../shared/models/payment-merchant';
import {paymentMerchantManagePageActions} from '../actions';

export interface State {
  paymentMerchantEntities: { [id: string]: PaymentMerchant };
}

export const initialState: State = {
  paymentMerchantEntities: {},
};

export function reducer(state = initialState, action: paymentMerchantManagePageActions.Actions): State {
  switch (action.type) {
    case paymentMerchantManagePageActions.INIT_SUCCESS: {
      const paymentMerchantEntities = (action.paymentMerchants || []).reduce((acc, cur) => {
        acc[cur.id] = cur;
        return acc;
      }, {});
      return {
        ...state,
        paymentMerchantEntities,
      };
    }

    default: {
      return state;
    }
  }
}

export const getPaymentMerchantEntities = (state: State) => state.paymentMerchantEntities;
export const getPaymentMerchants = createSelector(getPaymentMerchantEntities, entities => Object.keys(entities).map(it => entities[it]));
