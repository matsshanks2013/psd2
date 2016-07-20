package com.ibm.api.psd2.api.controllers;

import java.util.ArrayList;
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

import com.ibm.api.psd2.api.beans.ChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.payments.PaymentRequestBean;
import com.ibm.api.psd2.api.beans.payments.PaymentResponseBean;
import com.ibm.api.psd2.api.beans.payments.TxnChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.payments.TxnPartyBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestDetailsBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;
import com.ibm.api.psd2.api.beans.subscription.TransactionRequestTypeBean;
import com.ibm.api.psd2.api.beans.subscription.ViewIdBean;
import com.ibm.api.psd2.api.dao.PaymentsDao;
import com.ibm.api.psd2.api.dao.SubscriptionDao;
import com.ibm.api.psd2.api.integration.KafkaMessageProducer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@RestController
public class PaymentsController extends APIController
{
	private static final Logger logger = LogManager.getLogger(PaymentsController.class);

	@Autowired
	PaymentsDao pdao;

	@Autowired
	SubscriptionDao sdao;

	@Autowired
	KafkaMessageProducer kmp;

	@Value("${version}")
	String version;

	@Value("${kakfa.topic}")
	String topic;

	@Value("${psd2api.integration.useKafka}")
	private boolean useKafka;

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetailsBean> createTransactionRequest(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@RequestBody(required = true) TxnRequestBean trb, Authentication auth)
	{
		ResponseEntity<TxnRequestDetailsBean> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();

			ViewIdBean specifiedView = new ViewIdBean();
			specifiedView.setId(viewId);

			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			if (trb == null || trb.getTo() == null
					|| (accountId.equals(trb.getTo().getAccount_id()) && bankId.equals(trb.getTo().getBank_id())))
			{
				throw new IllegalArgumentException("Invalid Transaction Request");
			}

			TxnPartyBean payee = new TxnPartyBean(bankId, accountId);
			TxnRequestDetailsBean t = pdao.createTransactionRequest(sib, trb, payee, txnType);

			if (useKafka && t != null && TxnRequestDetailsBean.TXN_STATUS_PENDING.equalsIgnoreCase(t.getStatus()))
			{
				kmp.publishMessage(topic, t.getId(), t.toString());
			}

			response = ResponseEntity.ok(t);
		} catch (Exception ex)
		{
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
	
	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.POST, value = "pay/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<PaymentResponseBean> createPaymentRequest(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@RequestBody(required = true) PaymentRequestBean prb, Authentication auth)
	{
		ResponseEntity<PaymentResponseBean> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();

			ViewIdBean specifiedView = new ViewIdBean();
			specifiedView.setId(viewId);

			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}
			logger.info("prb");
			logger.info("From" + prb.getPaymentInfo()[0].getFrom());
			logger.info("check account match" + accountId.equals(prb.getPaymentInfo()[0].getFrom().getIban()));
			logger.info("swift bic match" + bankId.equals(prb.getPaymentInfo()[0].getFrom().getSwift_bic()));

			if (prb == null || prb.getPaymentInfo()[0].getFrom() == null
					|| (accountId.equals(prb.getPaymentInfo()[0].getFrom().getIban()) && bankId.equals(prb.getPaymentInfo()[0].getFrom().getSwift_bic())))
			{
				throw new IllegalArgumentException("Invalid Transaction Request");
			}

			
			PaymentResponseBean pres = pdao.createPaymentRequest(sib, prb, txnType);

		

			response = ResponseEntity.ok(pres);
		} catch (Exception ex)
		{
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}


	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests/{txnReqId}/challenge", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetailsBean> answerTransactionChallenge(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@PathVariable("txnReqId") String txnReqId, @RequestBody ChallengeAnswerBean t, Authentication auth)
	{
		ResponseEntity<TxnRequestDetailsBean> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewIdBean specifiedView = new ViewIdBean();
			specifiedView.setId(viewId);

			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			TxnRequestDetailsBean tdb = pdao.answerTransactionRequestChallenge(user, viewId, bankId, accountId, txnType,
					txnReqId, t);

			if (useKafka && tdb != null && TxnRequestDetailsBean.TXN_STATUS_PENDING.equalsIgnoreCase(tdb.getStatus()))
			{
				kmp.publishMessage(topic, tdb.getId(), tdb.toString());
			}

			response = ResponseEntity.ok(tdb);
		} catch (Exception ex)
		{
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TransactionRequestTypeBean>> getTransactionRequestTypes(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, Authentication auth)
	{
		ResponseEntity<List<TransactionRequestTypeBean>> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewIdBean specifiedView = new ViewIdBean();
			specifiedView.setId(viewId);

			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);

			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			response = ResponseEntity.ok(sib.getTransaction_request_types());
		} catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TxnRequestDetailsBean>> getTransactionRequests(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, Authentication auth)
	{
		ResponseEntity<List<TxnRequestDetailsBean>> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewIdBean specifiedView = new ViewIdBean();
			specifiedView.setId(viewId);

			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			List<TxnRequestDetailsBean> txns = pdao.getTransactionRequests(user, viewId, accountId, bankId);
			response = ResponseEntity.ok(txns);
		} catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
}
