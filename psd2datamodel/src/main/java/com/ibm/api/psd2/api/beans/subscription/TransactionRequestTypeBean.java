package com.ibm.api.psd2.api.beans.subscription;

import java.io.Serializable;

public class TransactionRequestTypeBean implements Serializable
{
	public static enum TYPES 
	{
		WITHIN_BANK("WITHIN_BANK"),
		INTER_BANK("INTER_BANK"),
		INTERNATIONAL("INTERNATIONAL");
		
		private String type;
		
		public String type()
		{
			return type;
		}
		
		TYPES(String type)
		{
			this.type = type;
		};
	}

	private String value;

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
	
	
}
