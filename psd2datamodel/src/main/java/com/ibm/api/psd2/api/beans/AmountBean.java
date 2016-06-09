package com.ibm.api.psd2.api.beans;

import java.io.Serializable;

public class AmountBean implements Serializable, ResponseBean
{
	/*
		"value":{
			"currency":"EUR",
			"amount":"100.53"
			},
	*/

	private String currency;
	private double amount;
	
	public String getCurrency()
	{
		return currency;
	}
	
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}
	
	public double getAmount()
	{
		return amount;
	}
	
	public void setAmount(double amount)
	{
		this.amount = amount;
	}
}
