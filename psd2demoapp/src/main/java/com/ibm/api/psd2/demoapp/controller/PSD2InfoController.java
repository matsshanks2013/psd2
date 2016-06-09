package com.ibm.api.psd2.demoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.psd2.demoapp.beans.APIResponse;
import com.ibm.api.psd2.demoapp.beans.PSD2Information;
import com.ibm.api.psd2.demoapp.db.PSD2InformationDao;


@RestController
public class PSD2InfoController extends APIController
{
	
	@Autowired
	PSD2InformationDao psd2InfoDao;
	
	@Value("${version}")
	private String version;	
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.GET, value = "/info/{tag}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse> getInfo(@PathVariable("tag") String tag)
	{
		APIResponse result = null;
		ResponseEntity<APIResponse> response;
		try
		{
			PSD2Information psd2info = psd2InfoDao.getInfo(tag);
			result = new APIResponse();
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(psd2info);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version);
		}
		return response;
	}
}
