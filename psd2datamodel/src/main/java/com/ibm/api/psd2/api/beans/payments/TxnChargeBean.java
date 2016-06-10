package com.ibm.api.psd2.api.beans.payments;

import java.io.Serializable;

import com.ibm.api.psd2.api.beans.AmountBean;

public class TxnChargeBean implements Serializable
{

	/*
		  "charge":{
		    "summary":"Total charges for completed transaction",
		    "value":{
		      "currency":"EUR",
		      "amount":"0.010053"
		    }
		  }
	*/
	
	private String summary;
	private AmountBean value;
	
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public AmountBean getValue()
	{
		return value;
	}
	public void setValue(AmountBean value)
	{
		this.value = value;
	}
	
	
}
