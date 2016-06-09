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
import com.ibm.api.psd2.api.beans.ResponseBean;
import com.ibm.api.psd2.api.beans.UUIDGenerator;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;
import com.ibm.api.psd2.api.beans.transaction.TxnChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.transaction.TxnChallengeBean;
import com.ibm.api.psd2.api.beans.transaction.TxnRequestDetailsBean;
import com.ibm.api.psd2.api.beans.transaction.TxnPartyBean;
import com.ibm.api.psd2.api.beans.transaction.TxnRequestBean;
import com.ibm.api.psd2.api.rules.PaymentRules;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

@Component
public class PaymentsDaoImpl implements PaymentsDao
{
	private static final Logger logger = LogManager.getLogger(PaymentsDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Autowired
	private SubscriptionDao sdao;

	@Autowired
	private PaymentRules pr;

	@Value("${mongodb.collection.payments}")
	private String payments;

	@Override
	public TxnRequestDetailsBean createTransactionRequest(TxnRequestBean trb, TxnPartyBean payee,
			String username, String viewId, String txnType) throws Exception
	{
		// Check if the from-party is owner of this account or not
		SubscriptionInfoBean sib = sdao.getSubscriptionInfo(username, viewId, payee.getAccount_id(),
				payee.getBank_id());

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
		coll.insertOne(mdp.format(t));
		return t;
	}

	@Override
	public TxnRequestDetailsBean answerTransactionRequestChallenge(String username, String viewId,
			String bankId, String accountId, String txnType, String txnReqId,
			TxnChallengeAnswerBean t) throws Exception
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
	public List<ResponseBean> getTransactionRequests(String username, String viewId,
			String accountId, String bankId) throws Exception
	{
		MongoCollection<Document> coll = conn.getDB().getCollection(payments);
		FindIterable<Document> iterable = coll
				.find(and(eq("from.account_id", accountId), eq("from.bank_id", bankId))).projection(excludeId());;

		List<ResponseBean> txns = null;

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
}
