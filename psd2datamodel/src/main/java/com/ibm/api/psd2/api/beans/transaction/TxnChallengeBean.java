package com.ibm.api.psd2.api.beans.transaction;

import java.io.Serializable;

import com.ibm.api.psd2.api.beans.ResponseBean;

public class TxnChallengeBean implements Serializable, ResponseBean
{
	private String id;
	private int allowed_attempts;
	private String challenge_type;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
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
}
