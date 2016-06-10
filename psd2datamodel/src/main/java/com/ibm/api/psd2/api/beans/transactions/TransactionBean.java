package com.ibm.api.psd2.api.beans.transactions;

import com.ibm.api.psd2.api.beans.account.BankAccountDetailsBean;

public class TransactionBean
{
	private String id;
	private BankAccountDetailsBean this_account;
	private BankAccountDetailsBean other_account;
	private TransactionDetailsBean details;

}
