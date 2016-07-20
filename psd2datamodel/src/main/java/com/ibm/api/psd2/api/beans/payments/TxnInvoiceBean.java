/**
 * 
 */
package com.ibm.api.psd2.api.beans.payments;

import java.util.Date;

/**
 * @author ibm
 *
 */
public class TxnInvoiceBean {
	
	private Date invoiceDate;
	private double number;
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public double getNumber() {
		return number;
	}
	public void setNumber(double number) {
		this.number = number;
	}
	
	

}
