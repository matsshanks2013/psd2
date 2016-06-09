package com.ibm.api.psd2.api.beans.transaction;

import java.io.Serializable;

import com.ibm.api.psd2.api.beans.AmountBean;
import com.ibm.api.psd2.api.beans.ResponseBean;

public class TxnChargeBean implements Serializable, ResponseBean
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
