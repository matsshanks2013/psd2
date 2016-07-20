package com.ibm.api.psd2.api.payments.utils;

public class PaymentInfo {
	
	 public DebitParty from;
     public CreditParty body;
	public DebitParty getFrom() {
		return from;
	}
	public void setFrom(DebitParty from) {
		this.from = from;
	}
	public CreditParty getBody() {
		return body;
	}
	public void setBody(CreditParty body) {
		this.body = body;
	}
     
     

}
