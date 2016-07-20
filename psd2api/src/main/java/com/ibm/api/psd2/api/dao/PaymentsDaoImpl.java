package com.ibm.api.psd2.api.dao;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ibm.api.psd2.api.Constants;
import com.ibm.api.psd2.api.beans.AmountBean;
import com.ibm.api.psd2.api.beans.ChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.UUIDGenerator;
import com.ibm.api.psd2.api.beans.payments.PaymentRequestBean;
import com.ibm.api.psd2.api.beans.payments.PaymentResponseBean;
import com.ibm.api.psd2.api.beans.payments.TxnChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.payments.TxnChallengeBean;
import com.ibm.api.psd2.api.beans.payments.TxnPartyBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestDetailsBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;

import com.ibm.api.psd2.api.rules.PaymentRules;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import com.ibm.api.psd2.api.beans.account.BankAccountDetailsBean;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PaymentsDaoImpl implements PaymentsDao
{
	private static final Logger logger = LogManager.getLogger(PaymentsDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Autowired
	private PaymentRules pr;
	


	@Value("${mongodb.collection.payments}")
	private String payments;

	@Override
	//@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public TxnRequestDetailsBean createTransactionRequest(SubscriptionInfoBean sib, TxnRequestBean trb, TxnPartyBean payee, String txnType) throws Exception
	{
		if (!pr.isTransactionTypeAllowed(sib, txnType))
		{
			throw new IllegalArgumentException(
					"Invalid Transaction Type Specified. Available Values are: " + StringUtils
							.collectionToCommaDelimitedString(sib.getTransaction_request_types()));
		}

		TxnRequestDetailsBean t = new TxnRequestDetailsBean();
		t.setBody(trb);
		t.setType(txnType);

		if (pr.checkLimit(trb, sib, txnType))
		{
			logger.info("Within Specified Limit. Hence Not Challenging the Request");
			t.setChallenge(null);
			t.setStatus(TxnRequestDetailsBean.TXN_STATUS_PENDING);
		}
		else
		{
			logger.info("Amount Greater than specified limit. Hence creating a challenge");
			TxnChallengeBean challenge = new TxnChallengeBean();
			challenge.setId(UUIDGenerator.generateUUID());
			challenge.setChallenge_type(t.getType());
			challenge.setAllowed_attempts(Constants.CHALLENGE_MAX_ATTEMPTS);
			t.setChallenge(challenge);
			t.setStatus(TxnRequestDetailsBean.TXN_STATUS_INITIATED);
		}

		t.setTransaction_ids(UUIDGenerator.generateUUID());
		t.setFrom(payee);
		t.setStart_date(new Date());
		t.setId(UUIDGenerator.generateUUID());
		t.setCharge(pr.getTransactionCharge(trb, payee));
		// Perform Limit validation.
		MongoCollection<Document> coll = conn.getDB().getCollection(payments);
		// calling transfer funds
		transferFunds(t);
		t.setStatus(TxnRequestDetailsBean.TXN_STATUS_COMPLETED);
		coll.insertOne(mdp.format(t));
		
		
		
		/**
		UpdateResult update = coll.updateOne(new Document("id", t.getTransaction_ids()),
				new Document("$set", new Document("status", TxnRequestDetailsBean.TXN_STATUS_COMPLETED).append("body.to.$value.currency", t.getBody().getValue().getCurrency())
						.append("body.to.$value.amount", t.getBody().getValue().getAmount())));
		logger.info("update count" + update.getModifiedCount());
		logger.info("matched count" + update.getMatchedCount());
		
		if (update.getModifiedCount() != 0)
		{
			t.setStatus(TxnRequestDetailsBean.TXN_STATUS_PENDING);
		}
		**/
		return t;
	}
	
	public PaymentResponseBean createPaymentRequest(SubscriptionInfoBean sib, PaymentRequestBean prb,
			 String txnType) throws Exception{
		
		if (!pr.isTransactionTypeAllowed(sib, txnType))
		{
			throw new IllegalArgumentException(
					"Invalid Transaction Type Specified. Available Values are: " + StringUtils
							.collectionToCommaDelimitedString(sib.getTransaction_request_types()));
		}

		PaymentResponseBean pres = new PaymentResponseBean();
	
		pres.setType(txnType);

		if (pr.checkLimit(prb, sib, txnType))
		{
			logger.info("Within Specified Limit. Hence Not Challenging the Request");
			pres.setChallenge(null);
			pres.setStatus(PaymentResponseBean.TXN_STATUS_PENDING);
		}
		else
		{
			logger.info("Amount Greater than specified limit. Hence creating a challenge");
			TxnChallengeBean challenge = new TxnChallengeBean();
			challenge.setId(UUIDGenerator.generateUUID());
			//challenge.setChallenge_type(prb.getPaymentInfo()[0].getBody().getType());
			challenge.setAllowed_attempts(Constants.CHALLENGE_MAX_ATTEMPTS);
			pres.setChallenge(challenge);
			pres.setStatus(TxnRequestDetailsBean.TXN_STATUS_INITIATED);
		}

		pres.setTransaction_ids(UUIDGenerator.generateUUID());
	
		pres.setStart_date(new Date());
		pres.setId(UUIDGenerator.generateUUID());
		//pres.setCharge(pr.getTransactionCharge(trb, payee)); TO-DO
		// Perform Limit validation.
		MongoCollection<Document> coll = conn.getDB().getCollection(payments);
		// calling transfer funds
		//transferFunds(t);// to -do
		pres.setStatus(PaymentResponseBean.TXN_STATUS_COMPLETED);
		coll.insertOne(mdp.format(pres));
		return pres;
		
	}
	

	
	/**
	 * Transfer funds
	 * @param trb
	 * @param bAccount
	 */

	private TxnRequestDetailsBean transferFunds(TxnRequestDetailsBean t) {
		// TODO Auto-generated method stub
		double debitAmount = new Double(t.getFrom().getAmount()).doubleValue();
		logger.info("debit amount" + debitAmount);
		double creditAmount = t.getBody().getValue().getAmount();
		logger.info("initial credit amount" + creditAmount);
		creditAmount = debitAmount + creditAmount;
		logger.info("creditAmount final" + creditAmount);
		AmountBean finalAmount = new AmountBean();
		finalAmount.setAmount(creditAmount);
		logger.info("currency in transfer funds" + t.getBody().getValue().getCurrency());
		finalAmount.setCurrency(t.getBody().getValue().getCurrency());
		t.getBody().setValue(finalAmount);
		
		return t;
	}

	@Override
	public TxnRequestDetailsBean answerTransactionRequestChallenge(String username, String viewId,
			String bankId, String accountId, String txnType, String txnReqId,
			ChallengeAnswerBean t) throws Exception
	{
		MongoCollection<Document> coll = conn.getDB().getCollection(payments);
		FindIterable<Document> iterable = coll
				.find(and(eq("from.account_id", accountId), eq("from.bank_id", bankId),
						eq("type", txnType), eq("id", txnReqId), eq("challenge.id", t.getId()))).projection(excludeId());

		TxnRequestDetailsBean tdb = null;
		for (Document document : iterable)
		{
			if (document != null)
			{
				tdb = mdp.parse(document, new TxnRequestDetailsBean());
			}
		}

		if (tdb == null)
		{
			throw new IllegalArgumentException("Specified Transaction Not Found");
		}

		if (!pr.validateTxnChallengeAnswer(t, username, accountId, bankId))
		{
			throw new IllegalArgumentException(
					"Incorrect Transaction Request Challenge Answer Specified");
		}

		UpdateResult update = coll.updateOne(new Document("id", txnReqId),
				new Document("$set", new Document("status", TxnRequestDetailsBean.TXN_STATUS_PENDING)));
		
		if (update.getModifiedCount() != 0)
		{
			tdb.setStatus(TxnRequestDetailsBean.TXN_STATUS_PENDING);
		}

		return tdb;

	}

	@Override
	public List<TxnRequestDetailsBean> getTransactionRequests(String username, String viewId,
			String accountId, String bankId) throws Exception
	{
		MongoCollection<Document> coll = conn.getDB().getCollection(payments);
		FindIterable<Document> iterable = coll
				.find(and(eq("from.account_id", accountId), eq("from.bank_id", bankId))).projection(excludeId());;

		List<TxnRequestDetailsBean> txns = null;

		for (Document document : iterable)
		{
			if (document != null)
			{
				if (txns == null)
				{
					txns = new ArrayList<>();
				}
				TxnRequestDetailsBean tdb = mdp.parse(document, new TxnRequestDetailsBean());
				txns.add(tdb);
			}
		}
		return txns;

	}


	@Override
	public TxnRequestDetailsBean paymentCancellation(String viewId, String bankId, String accountId,
			String txnType, String txnReqId) throws Exception {
		// TODO Auto-generated method stub
		MongoCollection<Document> coll = conn.getDB().getCollection(payments);
		FindIterable<Document> iterable = coll
				.find(and(eq("from.account_id", accountId), eq("from.bank_id", bankId),
						eq("type", txnType), eq("id", txnReqId))).projection(excludeId());

		TxnRequestDetailsBean tdb = null;
		for (Document document : iterable)
		{
			if (document != null)
			{
				tdb = mdp.parse(document, new TxnRequestDetailsBean());
			}
		}

		if (tdb == null)
		{
			throw new IllegalArgumentException("Specified Transaction Not Found");
		}

		logger.info("id in dao" + txnReqId);

		UpdateResult update = coll.updateOne(new Document("id", txnReqId),
				new Document("$set", new Document("status", TxnRequestDetailsBean.TXN_STATUS_CANCELLED)));
		

		return tdb;
	}


	@Override
	public String paymentStatus(String viewId, String bankId, String accountId,
			String txnReqId) throws Exception {
		// TODO Auto-generated method stub
		String paymentStatus = null;
		MongoCollection<Document> coll = conn.getDB().getCollection(payments);
		FindIterable<Document> iterable = coll
				.find(and(eq("from.account_id", accountId), eq("from.bank_id", bankId), eq("id", txnReqId))).projection(excludeId());;

		TxnRequestDetailsBean tdb = null;

		for (Document document : iterable)
		{
			if (document != null)
			{
				
				 tdb = mdp.parse(document, new TxnRequestDetailsBean());
				
			}
		}
		
		if (tdb == null)
		{
			throw new IllegalArgumentException("Specified Transaction Not Found");
		}
		else{
			paymentStatus = tdb.getStatus();
		}
		logger.info("payment status in dao" + paymentStatus);
		
		return paymentStatus;
	}
}
