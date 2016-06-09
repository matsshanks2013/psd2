package com.ibm.api.psd2.api.dao;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.api.psd2.api.beans.ResponseBean;
import com.ibm.api.psd2.api.beans.account.BankAccountDetailsBean;
import com.ibm.api.psd2.api.beans.account.BankAccountOverviewBean;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Component
public class BankAccountDaoImpl implements BankAccountDao
{

	private static final Logger logger = LogManager.getLogger(BankAccountDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.bankaccounts}")
	private String bankAccounts;

	@Override
	public BankAccountDetailsBean getBankAccountDetails(String bankId, String accountId)
			throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
		FindIterable<Document> iterable = coll.find(and(eq("id", accountId), eq("bank_id", bankId)))
				.projection(excludeId());
		BankAccountDetailsBean b = null;

		for (Document document : iterable)
		{
			if (document != null)
			{
				logger.info("message = " + document.toJson());
				b = mdp.parse(document, new BankAccountDetailsBean());
			}
		}

		return b;
	}

	@Override
	public ArrayList<ResponseBean> getBankAccounts(String username, String bank_id) throws Exception
	{
		logger.info("bankId = " + bank_id + ", username = " + username);
		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
		FindIterable<Document> iterable = coll
				.find(and(eq("username", username), eq("bank_id", bank_id)))
				.projection(excludeId());
		ArrayList<ResponseBean> accList = null;

		for (Document document : iterable)
		{
			if (document != null)
			{
				logger.info("message = " + document.toJson());
				BankAccountDetailsBean b = mdp.parse(document, new BankAccountDetailsBean());
				if (b != null)
				{
					BankAccountOverviewBean bo = new BankAccountOverviewBean();
					bo.setBank_id(b.getBank_id());
					bo.setId(b.getId());
					bo.setLabel(b.getLabel());

					if (accList == null)
					{
						accList = new ArrayList<>();
					}
					accList.add(bo);
				}
			}
		}

		return accList;
	}

	@Override
	public BankAccountDetailsBean getBankAccountDetails(String bankId, String accountId,
			String username) throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
		FindIterable<Document> iterable = coll
				.find(and(eq("id", accountId), eq("bank_id", bankId), eq("username", username)))
				.projection(excludeId());
		BankAccountDetailsBean b = null;

		for (Document document : iterable)
		{
			if (document != null)
			{
				logger.info("message = " + document.toJson());
			}
			b = mdp.parse(document, new BankAccountDetailsBean());
		}

		return b;
	}

	public void createBankAccountDetails(BankAccountDetailsBean b) throws Exception
	{
		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
		coll.insertOne(mdp.format(b));
	}

}
