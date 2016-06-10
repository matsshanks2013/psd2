package com.ibm.api.psd2.api.beans.transactions;

import java.io.Serializable;
import java.util.Date;

import com.ibm.api.psd2.api.beans.AmountBean;

public class TransactionDetailsBean implements Serializable
{
	private String type;
	private String description;
	private Date posted;
	private Date completed;
	private AmountBean new_balance;
	private AmountBean value;
	
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Date getPosted()
	{
		return posted;
	}
	public void setPosted(Date posted)
	{
		this.posted = posted;
	}
	public Date getCompleted()
	{
		return completed;
	}
	public void setCompleted(Date completed)
	{
		this.completed = completed;
	}
	public AmountBean getNew_balance()
	{
		return new_balance;
	}
	public void setNew_balance(AmountBean new_balance)
	{
		this.new_balance = new_balance;
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
