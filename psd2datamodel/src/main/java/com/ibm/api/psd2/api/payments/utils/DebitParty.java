/**
 * 
 */
package com.ibm.api.psd2.api.payments.utils;

/**
 * @author ibm
 *
 */
public class DebitParty {
	
	 public String iban;
     public String swift_bic;
     public String name;
     public String mop;
     public String code;
     public String execDate;
     public String country;
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
	public String getMop() {
		return mop;
	}
	public void setMop(String mop) {
		this.mop = mop;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getExecDate() {
		return execDate;
	}
	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
     
     

}
