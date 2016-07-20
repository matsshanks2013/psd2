package com.ibm.api.psd2.api.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class SimpleResponseBean implements Serializable
{
	public static final String CODE_SUCCESS = "success";
	public static final String CODE_ERROR = "error";
	
	private String responseCode;
	private String responseMessage;

	public String getResponseCode()
	{
		return responseCode;
	}

	public void setResponseCode(String code)
	{
		this.responseCode = code;
	}

	public String getResponseMessage()
	{
		return responseMessage;
	}

	public void setResponseMessage(String message)
	{
		this.responseMessage = message;
	}

}
