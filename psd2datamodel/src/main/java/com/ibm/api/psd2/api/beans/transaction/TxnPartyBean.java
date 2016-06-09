package com.ibm.api.psd2.api.beans.transaction;

import java.io.Serializable;

import com.ibm.api.psd2.api.beans.ResponseBean;

public class TxnPartyBean implements Serializable, ResponseBean
{
	/*
		"to":{
				"bank_id":"psd201-bank-x--uk",
	    		"account_id":"007007007007007007007"
			},  
	 */
	
	private String bank_id;
	private String account_id;
	
	public TxnPartyBean()
	{
		
	}
	
	public TxnPartyBean(String bank_id, String account_id)
	{
		this.bank_id = bank_id;
		this.account_id = account_id;
	}
	
	public String getBank_id()
	{
		return bank_id;
	}
	public void setBank_id(String bank_id)
	{
		this.bank_id = bank_id;
	}
	public String getAccount_id()
	{
		return account_id;
	}
	public void setAccount_id(String account_id)
	{
		this.account_id = account_id;
	}
	
	
}
