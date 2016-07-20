package com.ibm.api.psd2.api.rules;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

import com.ibm.api.psd2.api.Constants;
import com.ibm.api.psd2.api.beans.AmountBean;
import com.ibm.api.psd2.api.beans.ChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.payments.PaymentRequestBean;
import com.ibm.api.psd2.api.beans.payments.TxnChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.payments.TxnChargeBean;
import com.ibm.api.psd2.api.beans.payments.TxnPartyBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestDetailsBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;
import com.ibm.api.psd2.api.beans.subscription.TransactionLimitBean;
import com.ibm.api.psd2.api.beans.subscription.TransactionRequestTypeBean;
import com.ibm.api.psd2.api.dao.PaymentsDaoImpl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Component
public class PaymentRules {
	
	private static final Logger logger = LogManager.getLogger(PaymentRules.class);

	public boolean isSubscribed(SubscriptionInfoBean sib) {
		if (sib == null) {
			return false;
		}
		return true;
	}

	public boolean isTransactionTypeAllowed(SubscriptionInfoBean sib, String txnType) {
		for (Iterator<TransactionRequestTypeBean> iterator = sib.getTransaction_request_types().iterator(); iterator
				.hasNext();) {
			TransactionRequestTypeBean type = (TransactionRequestTypeBean) iterator.next();
			if (type.getValue().equals(txnType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * check for sufficient balance
	 * 
	 * @param sib
	 * @param bAccount
	 * @return
	 */
//	public boolean balanceCheck(TxnRequestBean trb, BankAccountDetailsBean bAccount) {
//
//		if (trb.getValue().getCurrency().equals(bAccount.getBalance().getCurrency())) {
//			if (trb.getValue().getAmount() > bAccount.getBalance().getAmount()) {
//				return false;
//			}
//		}
//
//		return true;
//
//	}
	
	public boolean balanceCheck(TxnRequestDetailsBean trdb) {
	
		logger.info("Inside balance check" + trdb);
		logger.info("currency" + trdb.getFrom().getCurrency());
		logger.info("from amount" + trdb.getFrom().getAmount());
		logger.info("to amount" +  trdb.getBody().getValue().getAmount());

		if (trdb.getFrom().getCurrency().equals(trdb.getBody().getValue().getCurrency())) {
			if (new Double(trdb.getFrom().getAmount()).doubleValue() > trdb.getBody().getValue().getAmount()) {
				return false;
			}
		}

		return true;

	}

	public boolean checkLimit(TxnRequestBean trb, SubscriptionInfoBean sib, String txnType) {

		ArrayList<TransactionLimitBean> limits = sib.getLimits();

		for (Iterator<TransactionLimitBean> iterator = limits.iterator(); iterator.hasNext();) {
			TransactionLimitBean limit = iterator.next();

			if (limit.getTransaction_request_type().getValue().equals(txnType)
					&& limit.getAmount().getCurrency().equals(trb.getValue().getCurrency())
					&& limit.getAmount().getAmount() > trb.getValue().getAmount()) {
				return true;
			}
		}
		return false;
	}
	public boolean checkLimit(PaymentRequestBean prb, SubscriptionInfoBean sib, String txnType) {

		ArrayList<TransactionLimitBean> limits = sib.getLimits();

		for (Iterator<TransactionLimitBean> iterator = limits.iterator(); iterator.hasNext();) {
			TransactionLimitBean limit = iterator.next();

			if (limit.getTransaction_request_type().getValue().equals(txnType)
					&& limit.getAmount().getCurrency().equals(prb.getPaymentInfo()[0].getBody().getValue().getCurrency())
					&& limit.getAmount().getAmount() > prb.getPaymentInfo()[0].getBody().getValue().getAmount()) {
				return true;
			}
		}
		return false;
	}

	public boolean checkTxnType(String txnType) {
		if (TransactionRequestTypeBean.TYPES.WITHIN_BANK.type().equals(txnType)
				|| TransactionRequestTypeBean.TYPES.INTER_BANK.type().equals(txnType)
				|| TransactionRequestTypeBean.TYPES.INTERNATIONAL.type().equals(txnType)) {
			return true;
		}
		return false;
	}

	public TxnChargeBean getTransactionCharge(TxnRequestBean trb, TxnPartyBean payee) {
		AmountBean ab = new AmountBean();
		// Currently hardcoded charge
		ab.setAmount(1.00);
		ab.setCurrency(Constants.CURRENCY_GBP);

		TxnChargeBean tcb = new TxnChargeBean();
		tcb.setSummary("Transaction Charge Summary");
		tcb.setValue(ab);
		return tcb;
	}

	public boolean validateTxnChallengeAnswer(ChallengeAnswerBean t, String user, String accountId, String bankId) {
		if (t.getAnswer() != null) {
			return true;
		}
		return false;
	}

}
