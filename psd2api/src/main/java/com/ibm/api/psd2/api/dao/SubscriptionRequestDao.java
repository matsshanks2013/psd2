package com.ibm.api.psd2.api.dao;

import com.ibm.api.psd2.api.beans.ChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionRequestBean;

public interface SubscriptionRequestDao
{
	public SubscriptionRequestBean getSubscriptionRequestByIdAndChallenge(String id, ChallengeAnswerBean cab) throws Exception;
	public SubscriptionRequestBean createSubscriptionRequest(SubscriptionRequestBean s) throws Exception;
	public long updateSubscriptionRequestStatus(String id, String status) throws Exception;

}
