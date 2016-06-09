package com.ibm.api.psd2.demoapp.beans;

import java.io.Serializable;

public class PSD2Information implements Serializable
{
	private String tag;
	private String info;
	private Object additionalInfo;
	
	public String getTag()
	{
		return tag;
	}
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	public String getInfo()
	{
		return info;
	}
	public void setInfo(String info)
	{
		this.info = info;
	}
	public Object getAdditionalInfo()
	{
		return additionalInfo;
	}
	public void setAdditionalInfo(Object additionalInfo)
	{
		this.additionalInfo = additionalInfo;
	}
	
}
