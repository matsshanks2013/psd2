package com.ibm.api.psd2.api.beans.payments;

import java.io.Serializable;

import com.ibm.api.psd2.api.beans.AmountBean;

public class TxnPartyBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5480649327067117693L;
	/*
	 * "to":{ "bank_id":"psd201-bank-x--uk",
	 * "account_id":"007007007007007007007" },
	 */

	private String bank_id;
	private String account_id;
	private String amount;
	private String currency;

	// added name

	private String name;

	public TxnPartyBean() {

	}

	public TxnPartyBean(String bank_id, String account_id) {
		this.bank_id = bank_id;
		this.account_id = account_id;

	}

	public TxnPartyBean(String bank_id, String account_id, String name, String amount, String currency) {
		this.bank_id = bank_id;
		this.account_id = account_id;
		this.name = name;
		this.amount = amount;
		this.currency = currency;

	}
	
	

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
