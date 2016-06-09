package com.ibm.api.psd2.api.dao;

import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;

public interface SubscriptionDao
{
	public SubscriptionInfoBean getSubscriptionInfo(String username, String viewId, String accountId, String bankId) throws Exception;
	public SubscriptionInfoBean getSubscriptionInfo(String username, String accountId, String bankId) throws Exception;
	public void createSubscriptionInfo(SubscriptionInfoBean s) throws Exception;


}
