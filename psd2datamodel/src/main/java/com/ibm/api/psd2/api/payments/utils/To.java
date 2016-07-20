package com.ibm.api.psd2.api.payments.utils;

public class To {
	
	public String txnReqId;
    public String iban;
    public String swift_bic;
    public String name;
    public String inv_no;
    public String inv_date;
	public String getTxnReqId() {
		return txnReqId;
	}
	public void setTxnReqId(String txnReqId) {
		this.txnReqId = txnReqId;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getSwift_bic() {
		return swift_bic;
	}
	public void setSwift_bic(String swift_bic) {
		this.swift_bic = swift_bic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInv_no() {
		return inv_no;
	}
	public void setInv_no(String inv_no) {
		this.inv_no = inv_no;
	}
	public String getInv_date() {
		return inv_date;
	}
	public void setInv_date(String inv_date) {
		this.inv_date = inv_date;
	}


}
