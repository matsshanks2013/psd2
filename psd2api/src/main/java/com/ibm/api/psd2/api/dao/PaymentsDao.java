package com.ibm.api.psd2.api.dao;

import java.util.ArrayList;
import java.util.List;

import com.ibm.api.psd2.api.beans.ResponseBean;
import com.ibm.api.psd2.api.beans.transaction.TxnChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.transaction.TxnRequestDetailsBean;
import com.ibm.api.psd2.api.beans.transaction.TxnPartyBean;
import com.ibm.api.psd2.api.beans.transaction.TxnRequestBean;

public interface PaymentsDao
{
	public TxnRequestDetailsBean createTransactionRequest(TxnRequestBean trb, TxnPartyBean payee,
			String username, String viewId, String txnType) throws Exception;

	public List<ResponseBean> getTransactionRequests(String username, String viewId,
			String accountId, String bankId) throws Exception;

	public TxnRequestDetailsBean answerTransactionRequestChallenge(String username, String viewId,
			String bankId, String accountId, String txnType, String txnReqId,
			TxnChallengeAnswerBean t) throws Exception;

}
