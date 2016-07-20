package com.ibm.api.psd2.api.beans.subscription;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.api.psd2.api.beans.ChallengeBean;

@JsonInclude(value = Include.NON_EMPTY)
public class SubscriptionRequestBean implements Serializable
{
	
	public static final String STATUS_INITIATED = "INITIATED";
	public static final String STATUS_SUBSCRIBED = "SUBSCRIBED";
	public static final String STATUS_REJECTED = "REJECTED";
	
	private String id;
	private SubscriptionInfoBean subscriptionInfo;
	private Date creationDate;
	private Date updatedDate;
	private String status;
	private ChallengeBean challenge;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public SubscriptionInfoBean getSubscriptionInfo()
	{
		return subscriptionInfo;
	}

	public void setSubscriptionInfo(SubscriptionInfoBean subscriptionInfo)
	{
		this.subscriptionInfo = subscriptionInfo;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public ChallengeBean getChallenge()
	{
		return challenge;
	}

	public void setChallenge(ChallengeBean challenge)
	{
		this.challenge = challenge;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubscriptionRequestBean other = (SubscriptionRequestBean) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public Date getUpdatedDate()
	{
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate)
	{
		this.updatedDate = updatedDate;
	}

	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return "";
	}

}
