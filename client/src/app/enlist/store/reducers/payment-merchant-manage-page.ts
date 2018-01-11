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
      const paymentMerchantEntities = PaymentMerchant.toEntities(action.paymentMerchants);
      return {...state, paymentMerchantEntities};
    }
    case paymentMerchantManagePageActions.DELETE_MANAGER_SUCCESS: {
      const {paymentMerchantId, managerId} = action.payload;
      const paymentMerchantEntities = {...state.paymentMerchantEntities};
      const paymentMerchant = paymentMerchantEntities[paymentMerchantId];
      if (!paymentMerchant) {
        return state;
      }
      paymentMerchant.managers = paymentMerchant.managers.filter(it => it.id !== managerId);
      paymentMerchantEntities[paymentMerchantId] = paymentMerchant;
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
