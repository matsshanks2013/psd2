package com.ibm.api.psd2.api.dao;

import java.util.List;

import com.ibm.api.psd2.api.beans.transactions.TransactionBean;

public interface TransactionDao
{
	public TransactionBean getTransactionById(String bankId, String accountId, String txnId) throws Exception;

	public List<TransactionBean> getTransactions(String bankId, String accountId, String sortDirection, Integer limit, String fromDate,
			String toDate, String sortBy, Integer number) throws Exception;

}
