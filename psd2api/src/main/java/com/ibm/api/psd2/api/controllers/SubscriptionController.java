package com.ibm.api.psd2.api.controllers;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;
import com.ibm.api.psd2.api.beans.subscription.TransactionLimitBean;
import com.ibm.api.psd2.api.dao.SubscriptionDao;

@RestController
public class SubscriptionController extends APIController
{
	@Autowired
	SubscriptionDao sdao;
	
	private static final Logger logger = LogManager.getLogger(SubscriptionController.class);

	@Value("${version}")
	private String version;
		
	@RequestMapping(method = RequestMethod.POST, value = "/admin/subscription", produces = MediaType.ALL_VALUE)
	public @ResponseBody ResponseEntity<String> createSubscription(@RequestBody SubscriptionInfoBean s)
	{
		ResponseEntity<String> response;
		try
		{
			sdao.createSubscriptionInfo(s);
			response = ResponseEntity.ok("SUCCESS");
		}
		catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body(e.getMessage());
		}
		return response;
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/admin/callback", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> decodeCode(@RequestParam(value="code") String code, @RequestParam(value="state") String state)
	{
		ResponseEntity<String> response;
		String resp = "{ \"code\": \"" + code + "\", \"state\": \"" + state + "\"}";
		response = ResponseEntity.ok(resp);
		return response;
	}
	
}
