package com.ibm.api.psd2.api.beans.subscription;

import java.io.Serializable;

import com.ibm.api.psd2.api.beans.AmountBean;

public class TransactionLimitBean implements Serializable
{

	private TransactionRequestTypeBean transaction_request_type;
	private AmountBean amount;
	
	public TransactionRequestTypeBean getTransaction_request_type()
	{
		return transaction_request_type;
	}
	public void setTransaction_request_type(TransactionRequestTypeBean transaction_request_type)
	{
		this.transaction_request_type = transaction_request_type;
	}
	public AmountBean getAmount()
	{
		return amount;
	}
	public void setAmount(AmountBean amount)
	{
		this.amount = amount;
	}
	
	
}
