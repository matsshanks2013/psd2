package com.ibm.api.psd2.api.beans.transactions;

import java.io.Serializable;

public class TransactionBean implements Serializable
{
	private String id;
	private TransactionAccountBean this_account;
	private TransactionAccountBean other_account;
	private TransactionDetailsBean details;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public TransactionAccountBean getThis_account()
	{
		return this_account;
	}
	public void setThis_account(TransactionAccountBean this_account)
	{
		this.this_account = this_account;
	}
	public TransactionAccountBean getOther_account()
	{
		return other_account;
	}
	public void setOther_account(TransactionAccountBean other_account)
	{
		this.other_account = other_account;
	}
	public TransactionDetailsBean getDetails()
	{
		return details;
	}
	public void setDetails(TransactionDetailsBean details)
	{
		this.details = details;
	}

	
}
