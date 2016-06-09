package com.ibm.api.psd2.api.beans.account;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.api.psd2.api.beans.AmountBean;
import com.ibm.api.psd2.api.beans.ResponseBean;

public class BankAccountDetailsBean implements Serializable, ResponseBean
{
	private String id;
	private String label;
	private String number;
	private ArrayList<BankAccountOwnerBean> owners;
	private String type;
	private AmountBean balance;
	private String iban;
	private String swift_bic;
	private String bank_id;
	private ArrayList<AmountBean> limits;
	private String username;
	
	public void addOwners(BankAccountOwnerBean b)
	{
		if (owners == null)
		{
			owners = new ArrayList<>();
		}
		owners.add(b);
	}
	
	public void addLimits(AmountBean ab)
	{
		if (limits == null)
		{
			limits = new ArrayList<>();
		}
		
		limits.add(ab);
	}

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

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public ArrayList<BankAccountOwnerBean> getOwners()
	{
		return owners;
	}

	public void setOwners(ArrayList<BankAccountOwnerBean> owners)
	{
		this.owners = owners;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public AmountBean getBalance()
	{
		return balance;
	}

	public void setBalance(AmountBean balance)
	{
		this.balance = balance;
	}

	public String getIban()
	{
		return iban;
	}

	public void setIban(String iban)
	{
		this.iban = iban;
	}

	public String getSwift_bic()
	{
		return swift_bic;
	}

	public void setSwift_bic(String swift_bic)
	{
		this.swift_bic = swift_bic;
	}

	public String getBank_id()
	{
		return bank_id;
	}

	public void setBank_id(String bank_id)
	{
		this.bank_id = bank_id;
	}
	
	public ArrayList<AmountBean> getLimits()
	{
		return limits;
	}

	public void setLimits(ArrayList<AmountBean> limits)
	{
		this.limits = limits;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
