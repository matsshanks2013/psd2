package com.ibm.api.psd2.api.response;

import com.ibm.api.psd2.api.beans.Visitor;
import com.ibm.api.psd2.api.beans.account.BankAccountDetailsBean;
import com.ibm.api.psd2.api.beans.account.BankAccountOverviewBean;

public class BankAccountOverviewVisitor implements Visitor
{

	@Override
	public <T, U> T visit(U u)
	{
		BankAccountOverviewBean baob = new BankAccountOverviewBean();
		BankAccountDetailsBean bad = (BankAccountDetailsBean) u;
		
		baob.setBank_id(bad.getBank_id());
		baob.setId(bad.getId());
		baob.setLabel(bad.getLabel());
		
		return (T) baob;
	}

	
}
