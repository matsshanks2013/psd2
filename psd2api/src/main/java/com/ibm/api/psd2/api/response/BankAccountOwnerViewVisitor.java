package com.ibm.api.psd2.api.response;

import com.ibm.api.psd2.api.beans.Visitor;
import com.ibm.api.psd2.api.beans.account.*;

public class BankAccountOwnerViewVisitor implements Visitor
{
	@Override
	public <T, U> T visit(U u)
	{
		
		BankAccountDetailsViewBean badv = new BankAccountDetailsViewBean();
		BankAccountDetailsBean bad = (BankAccountDetailsBean) u;
		
		badv.setId(bad.getId());
		badv.setBank_id(bad.getBank_id());
		badv.setBalance(bad.getBalance());
		badv.setIban(bad.getIban());
		badv.setLabel(bad.getLabel());
		badv.setNumber(bad.getNumber());
		badv.setOwners(bad.getOwners());
		badv.setSwift_bic(bad.getSwift_bic());
		badv.setType(bad.getType());
		return (T) badv;
	}
	
}
