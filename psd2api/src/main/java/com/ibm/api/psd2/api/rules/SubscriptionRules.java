package com.ibm.api.psd2.api.rules;

import org.springframework.stereotype.Component;

import com.ibm.api.psd2.api.beans.ChallengeAnswerBean;

@Component
public class SubscriptionRules
{
	public boolean validateTxnChallengeAnswer(ChallengeAnswerBean t)
	{
		if (t.getAnswer() != null)
		{
			return true;
		}
		return false;
	}

}
