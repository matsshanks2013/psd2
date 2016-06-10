package com.ibm.api.psd2.api.beans.account;

import java.io.Serializable;

public class BankAccountOverviewBean implements Serializable
{
	private String id;
	private String label;
	private String bank_id;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getLabel()
	{
		return label;
	}
	public void setLabel(String label)
	{
		this.label = label;
	}
	public String getBank_id()
	{
		return bank_id;
	}
	public void setBank_id(String bank_id)
	{
		this.bank_id = bank_id;
	}
	
	
}
