/**
 * 
 */
package com.ibm.api.psd2.api.payments.utils;

/**
 * @author ibm
 *
 */
public class CreditParty {
	
	 public To to;
     public Value value;
     public String description;
     public String transaction_request_type;
	public To getTo() {
		return to;
	}
	public void setTo(To to) {
		this.to = to;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTransaction_request_type() {
		return transaction_request_type;
	}
	public void setTransaction_request_type(String transaction_request_type) {
		this.transaction_request_type = transaction_request_type;
	}
     
     

}
