package com.ibm.api.psd2.api.controllers;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.psd2.api.Constants;
import com.ibm.api.psd2.api.beans.ResponseBean;
import com.ibm.api.psd2.api.beans.account.BankAccountDetailsBean;
import com.ibm.api.psd2.api.beans.account.BankAccountDetailsOwnerViewBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;
import com.ibm.api.psd2.api.dao.BankAccountDao;
import com.ibm.api.psd2.api.dao.SubscriptionDao;

@RestController
public class BankAccountController extends APIController
{
	private static final Logger logger = LogManager.getLogger(BankAccountController.class);

	@Autowired
	BankAccountDao bad;
	
	@Autowired
	SubscriptionDao sdao;

	@Value("${version}")
	private String version;

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/my/banks/{bankId}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<ResponseBean>> getBankAccounts(
			@PathVariable("bankId") String bankId, Authentication auth)
	{
		ResponseEntity<List<ResponseBean>> response;
		try
		{
			//@Todo: Check if the user is authorized to view this account or not.
			
			String user = (String) auth.getPrincipal();
			List<ResponseBean> ba = bad.getBankAccounts(user, bankId);
			response = ResponseEntity.ok(ba);
		}
		catch (Exception ex)
		{
			response = handleExceptions(ex);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<ResponseBean> getAccountById(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, Authentication auth)
	{
		ResponseEntity<ResponseBean> response;
		try
		{			
			String user = (String) auth.getPrincipal();
			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, viewId, accountId, bankId);
			if (sib == null)
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			BankAccountDetailsBean b = bad.getBankAccountDetails(bankId, accountId, user);
			BankAccountDetailsOwnerViewBean bo = null;
			
			if (b == null)
			{
				throw new IllegalArgumentException("Account Not Found");
			}
			
			if (Constants.OWNER_VIEW.equals(viewId))
			{
				bo = new BankAccountDetailsOwnerViewBean(b);
				response = ResponseEntity.ok(bo);
			}
			else
			{
				throw new IllegalArgumentException("View ID is incorrect. Currently supported ones are: " + Constants.OWNER_VIEW);
			}
		}
		catch (Exception ex)
		{
			response = handleException(ex);
		}
		return response;
	}
	
	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/my/banks/{bankId}/accounts/{accountId}/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<ResponseBean> getAccountById(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,Authentication auth)
	{
		ResponseEntity<ResponseBean> response;
		try
		{			
			String user = (String) auth.getPrincipal();
			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, accountId, bankId);
			if (sib == null)
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			BankAccountDetailsBean b = bad.getBankAccountDetails(bankId, accountId, user);
			BankAccountDetailsOwnerViewBean bo = null;
			
			if (b == null)
			{
				throw new IllegalArgumentException("Account Not Found");
			}
			
			bo = new BankAccountDetailsOwnerViewBean(b);
			response = ResponseEntity.ok(bo);
		}
		catch (Exception ex)
		{
			response = handleException(ex);
		}
		return response;
	}	
	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/account", produces = MediaType.ALL_VALUE)
	public @ResponseBody ResponseEntity<String> createAccount(@RequestBody BankAccountDetailsBean b)
	{
		ResponseEntity<String> response;
		try
		{
			//@Todo: Check if the user is authorized to view this account or not.

			if (b == null)
			{
				throw new IllegalArgumentException("No Account Specified");
			}
			
			logger.info("BankAccountDetailsBean = " + b.toString());
			
			bad.createBankAccountDetails(b);
			response= ResponseEntity.ok("SUCCESS");
		}
		catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body("ERROR: " + e.getMessage());
		}
		return response;
	}
}
