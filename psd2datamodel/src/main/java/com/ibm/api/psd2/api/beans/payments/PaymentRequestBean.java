/**
 * 
 */
package com.ibm.api.psd2.api.beans.payments;

import com.ibm.api.psd2.api.payments.utils.PaymentInfo;

/**
 * @author ibm
 *
 */
public class PaymentRequestBean {

	public String message_id;
	public String start_date;
	public String totalNumberTransac;
	public String name;
	public String paymentId;
	public String controlsum;
	public String txnReqId;
	public PaymentInfo paymentInfo[];
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getTotalNumberTransac() {
		return totalNumberTransac;
	}
	public void setTotalNumberTransac(String totalNumberTransac) {
		this.totalNumberTransac = totalNumberTransac;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getControlsum() {
		return controlsum;
	}
	public void setControlsum(String controlsum) {
		this.controlsum = controlsum;
	}
	public String getTxnReqId() {
		return txnReqId;
	}
	public void setTxnReqId(String txnReqId) {
		this.txnReqId = txnReqId;
	}
	public PaymentInfo[] getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(PaymentInfo[] paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
	

}
