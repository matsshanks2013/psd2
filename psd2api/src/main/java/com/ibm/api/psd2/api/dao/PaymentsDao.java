package com.ibm.api.psd2.api.dao;

import java.util.List;

import com.ibm.api.psd2.api.beans.ChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.account.BankAccountDetailsBean;
import com.ibm.api.psd2.api.beans.payments.PaymentRequestBean;
import com.ibm.api.psd2.api.beans.payments.PaymentResponseBean;
import com.ibm.api.psd2.api.beans.payments.TxnChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.payments.TxnPartyBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestDetailsBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;

public interface PaymentsDao
{
	public TxnRequestDetailsBean createTransactionRequest(SubscriptionInfoBean sib, TxnRequestBean trb,
			TxnPartyBean payee, String txnType) throws Exception;
	
	public PaymentResponseBean createPaymentRequest(SubscriptionInfoBean sib, PaymentRequestBean prb,
			 String txnType) throws Exception;
	
//	public TxnRequestDetailsBean paymentTransfer(TxnRequestBean trb,
//			TxnPartyBean payee, String txnType, String txnReqId) throws Exception;

	public List<TxnRequestDetailsBean> getTransactionRequests(String username, String viewId, String accountId,
			String bankId) throws Exception;

	
	public TxnRequestDetailsBean paymentCancellation(String viewId, String bankId,
			String accountId, String txnType, String txnReqId) throws Exception;
	
	public String paymentStatus(String viewId, String bankId,
			String accountId, String txnReqId) throws Exception;

	TxnRequestDetailsBean answerTransactionRequestChallenge(String username, String viewId, String bankId,
			String accountId, String txnType, String txnReqId, ChallengeAnswerBean t) throws Exception;

}
