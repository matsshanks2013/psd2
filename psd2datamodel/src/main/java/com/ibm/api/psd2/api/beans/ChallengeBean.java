package com.ibm.api.psd2.api.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@JsonInclude(value = Include.NON_EMPTY)
public class ChallengeBean implements Serializable
{
	private String cid;
	private int allowed_attempts;
	private String challenge_type;
	

	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public int getAllowed_attempts()
	{
		return allowed_attempts;
	}
	public void setAllowed_attempts(int allowed_attempts)
	{
		this.allowed_attempts = allowed_attempts;
	}
	public String getChallenge_type()
	{
		return challenge_type;
	}
	public void setChallenge_type(String challenge_type)
	{
		this.challenge_type = challenge_type;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
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
		ChallengeBean other = (ChallengeBean) obj;
		if (cid == null)
		{
			if (other.cid != null)
				return false;
		} else if (!cid.equals(other.cid))
			return false;
		return true;
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
