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

import com.ibm.api.psd2.api.Constants;
import com.ibm.api.psd2.api.dao.MongoConnection;
import com.ibm.api.psd2.api.dao.MongoDocumentParser;
import com.ibm.api.psd2.api.beans.ChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.ChallengeBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestDetailsBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionRequestBean;
import com.ibm.api.psd2.api.beans.UUIDGenerator;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

@Component
public class SubscriptionRequestDaoImpl implements SubscriptionRequestDao
{
	private static final Logger logger = LogManager.getLogger(SubscriptionRequestDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.subscriptionRequests}")
	private String subscriptionRequests;
	
	

	@Override
	public SubscriptionRequestBean getSubscriptionRequestByIdAndChallenge(String id, ChallengeAnswerBean cab) throws Exception
	{
		logger.info("id = " + id);
		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptionRequests);
		FindIterable<Document> iterable = coll.find(
				and(eq("id", id), eq("challenge.id", cab.getId())))
				.projection(excludeId());
		
		SubscriptionRequestBean s = null;

		Document document = iterable.first();
		if (document != null)
		{
			logger.info("message = " + document.toJson());
			s = mdp.parse(document, new SubscriptionRequestBean());
		}
		return s;
	}
	
	@Override
	public SubscriptionRequestBean createSubscriptionRequest(SubscriptionRequestBean s) throws Exception
	{
		logger.info("Subscription Request = " + s);
		
		s.setId(UUIDGenerator.generateUUID());
		s.setCreationDate(new Date());
		s.setStatus(SubscriptionRequestBean.STATUS_INITIATED);
		
		
		ChallengeBean c = new ChallengeBean();
		c.setCid(UUIDGenerator.generateUUID());
		c.setChallenge_type("NEW_SUBSCRIPTION");
		c.setAllowed_attempts(Constants.CHALLENGE_MAX_ATTEMPTS);
		
		s.setChallenge(c);
		logger.info("challenge" + s.getChallenge());
		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptionRequests);
		logger.info("Got the collection" + s);
		Document doc = mdp.format(s);
		logger.info("after formatting" + doc);
		coll.insertOne(mdp.format(doc));
		logger.info("insertion done");
		
		return s;
	}

	@Override
	public long updateSubscriptionRequestStatus(String id, String status) throws Exception
	{
		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptionRequests);

		UpdateResult update = coll.updateOne(new Document("id", id),
				new Document("$set", new Document("status", status)).append("$currentDate", new Document("updatedDate", true)));
		return update.getModifiedCount();
	}

	
}
