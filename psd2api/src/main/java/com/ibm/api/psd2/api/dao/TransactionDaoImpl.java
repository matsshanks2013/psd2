package com.ibm.api.psd2.api.dao;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.api.psd2.api.beans.transactions.TransactionBean;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Component
public class TransactionDaoImpl implements TransactionDao
{
	private static final Logger logger = LogManager.getLogger(TransactionDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.transactions}")
	private String transactions;


	public TransactionBean getTransactionById(String bankId, String accountId, String txnId) throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId + ", txnId = " + txnId);
		MongoCollection<Document> coll = conn.getDB().getCollection(transactions);
		
		FindIterable<Document> iterable = coll.find(and(eq("id", txnId), eq("this_account.id", accountId), eq("this_account.bank.national_identifier", bankId)))
				.projection(excludeId());
		TransactionBean t = null;

		if (iterable != null)
		{
			Document document = iterable.first();
			if (document != null)
			{
				logger.info("Transaction = " + document.toJson());
				t = mdp.parse(document, new TransactionBean());
			}
		}

		return t;
	}

	public List<TransactionBean> getTransactions(String bankId, String accountId, String sortDirection,int limit, String fromDate, String toDate, String sortBy, int number) throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		MongoCollection<Document> coll = conn.getDB().getCollection(transactions);
		
		
//		List<Bson> criteria = new ArrayList<>();
//		criteria.add(eq("this_account.id", accountId));
//		criteria.add(eq("this_account.bank.national_identifier", bankId));

		FindIterable<Document> iterable = coll.find(and(eq("this_account.id", accountId), eq("this_account.bank.national_identifier", bankId)))
				.projection(excludeId());
		List<TransactionBean> lst = null;
		for (Document document : iterable)
		{
			if (document != null)
			{
				if (lst == null)
				{
					lst = new ArrayList<>();
				}
				logger.info("Transaction = " + document.toJson());
				TransactionBean t = mdp.parse(document, new TransactionBean());
				lst.add(t);
			}
		}

		return lst;
	}




}
