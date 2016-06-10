package com.ibm.api.psd2.api.dao;

import java.util.List;

import com.ibm.api.psd2.api.beans.payments.TxnChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.payments.TxnPartyBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestDetailsBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;

public interface PaymentsDao
{
	public TxnRequestDetailsBean createTransactionRequest(SubscriptionInfoBean sib, TxnRequestBean trb,
			TxnPartyBean payee, String txnType) throws Exception;

	public List<TxnRequestDetailsBean> getTransactionRequests(String username, String viewId, String accountId,
			String bankId) throws Exception;

	public TxnRequestDetailsBean answerTransactionRequestChallenge(String username, String viewId, String bankId,
			String accountId, String txnType, String txnReqId, TxnChallengeAnswerBean t) throws Exception;

}
