package com.ibm.api.psd2.api.controllers;


import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import com.ibm.api.psd2.api.beans.ErrorBean;
import com.ibm.api.psd2.api.beans.ResponseBean;


public abstract class APIController
{
	private static final Logger logger = LogManager.getLogger(SubscriptionController.class);

	protected ResponseEntity<ResponseBean> handleException(Throwable e)
	{
		ResponseEntity<ResponseBean> response;
		ErrorBean eb = new ErrorBean();
		logger.error(e.getMessage(), e);
		eb.setError(e.getMessage());
		response = ResponseEntity.badRequest().body(eb);
		return response;
	}

	protected ResponseEntity<List<ResponseBean>> handleExceptions(Throwable e)
	{
		ResponseEntity<List<ResponseBean>> response;
		ErrorBean eb = new ErrorBean();
		logger.error(e.getMessage(), e);
		eb.setError(e.getMessage());
		List<ResponseBean> rl = Arrays.asList(eb);
		response = ResponseEntity.badRequest().body(rl);
		return response;
	}

	protected ResponseEntity<ResponseBean> handleException(Throwable e, boolean badRequest)
	{

		ResponseEntity<ResponseBean> response;
		ErrorBean eb = new ErrorBean();
		logger.error(e.getMessage(), e);
		eb.setError(e.getMessage());
		response = ResponseEntity.badRequest().body(eb);
		response = (badRequest) ? ResponseEntity.badRequest().body(eb)
				: ResponseEntity.ok().body(eb);
		return response;
	}

}
