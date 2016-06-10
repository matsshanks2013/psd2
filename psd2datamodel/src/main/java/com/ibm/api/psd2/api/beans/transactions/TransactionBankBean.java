package com.ibm.api.psd2.api.beans.transactions;

import java.io.Serializable;

public class TransactionBankBean implements Serializable
{
	String national_identifier;
	String name;
	public String getNational_identifier()
	{
		return national_identifier;
	}
	public void setNational_identifier(String national_identifier)
	{
		this.national_identifier = national_identifier;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	
}
