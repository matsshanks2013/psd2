package com.ibm.api.psd2.api.controllers;


import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import com.ibm.api.psd2.api.beans.ErrorBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;
import com.ibm.api.psd2.api.beans.subscription.ViewIdBean;


public abstract class APIController
{
	private static final Logger logger = LogManager.getLogger(APIController.class);

	protected ResponseEntity<Object> handleException(Throwable e)
	{
		ResponseEntity<Object> response;
		ErrorBean eb = new ErrorBean();
		logger.error(e.getMessage(), e);
		eb.setError(e.getMessage());
		response = ResponseEntity.badRequest().body(eb);
		return response;
	}

	protected ResponseEntity<List<Object>> handleExceptions(Throwable e)
	{
		ResponseEntity<List<Object>> response;
		ErrorBean eb = new ErrorBean();
		logger.error(e.getMessage(), e);
		eb.setError(e.getMessage());
		List<Object> rl = Arrays.asList(eb);
		response = ResponseEntity.badRequest().body(rl);
		return response;
	}

	protected ResponseEntity<Object> handleException(Throwable e, boolean badRequest)
	{

		ResponseEntity<Object> response;
		ErrorBean eb = new ErrorBean();
		logger.error(e.getMessage(), e);
		eb.setError(e.getMessage());
		response = ResponseEntity.badRequest().body(eb);
		response = (badRequest) ? ResponseEntity.badRequest().body(eb)
				: ResponseEntity.ok().body(eb);
		return response;
	}
	
	protected boolean validateSubscription(SubscriptionInfoBean s)
	{
		if (s != null && SubscriptionInfoBean.STATUS_ACTIVE.equals(s.getStatus()))
		{
			return true;
		}

		return false;
	}

	protected boolean validateSubscription(SubscriptionInfoBean s, ViewIdBean view)
	{
		logger.info("Method Arguments = " + s);
		logger.info("Method Arguments = " + view);

		if (s != null && view != null && SubscriptionInfoBean.STATUS_ACTIVE.equals(s.getStatus())
				&& s.getViewIds() != null && !s.getViewIds().isEmpty() && s.getViewIds().contains(view))
		{
			return true;
		}

		return false;
	}

}
