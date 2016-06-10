package com.ibm.api.psd2.api.beans;

import java.io.Serializable;

public class ErrorBean implements Serializable
{
	String error;

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}
	
	
}
