package com.ibm.api.psd2.demoapp.controller;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.psd2.demoapp.beans.APIResponse;
import com.ibm.api.psd2.demoapp.beans.UserInfo;
import com.ibm.api.psd2.demoapp.db.UserDetailsServiceImpl;

@RestController
public class PSD2DemoAppUserController extends APIController
{
	private static final Logger logger = LogManager.getLogger(PSD2DemoAppUserController.class);

	@Autowired
	UserDetailsServiceImpl uds;

	@Value("${version}")
	private String version;

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.GET, value = "/user/profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse> getUserProfile(Authentication auth)
	{
		APIResponse result = null;
		ResponseEntity<APIResponse> response;
		try
		{
			User user = (User) auth.getPrincipal();

			UserInfo userProfile = uds.getUserInfo(user.getUsername());
			result = new APIResponse();
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(userProfile);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version);
		}
		return response;
	}

	@PreAuthorize("permitAll()")
	@RequestMapping(method = RequestMethod.PUT, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse> addUser(
			@RequestBody(required = true) UserInfo userInfo)
	{
		APIResponse result = null;
		ResponseEntity<APIResponse> response;
		try
		{
			userInfo.setAccountNonExpired(true);
			userInfo.setAccountNonLocked(true);
			userInfo.setCredentialsNonExpired(true);

			uds.addUser(userInfo);
			result = new APIResponse();
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version);
		}
		return response;
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.PATCH, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse> updateUser(@RequestBody(required = true) UserInfo userInfo, Authentication auth)
	{
		APIResponse result = null;
		ResponseEntity<APIResponse> response;
		try
		{
			User user = (User) auth.getPrincipal();
			logger.info("Updating user: " + user.getUsername());
			
			userInfo.setAccountNonExpired(user.isAccountNonExpired());
			userInfo.setAccountNonLocked(user.isAccountNonLocked());
			userInfo.setCredentialsNonExpired(user.isCredentialsNonExpired());
			
			//Don't allow to update user other than the one who is authenticated
			userInfo.setEmail(user.getUsername());
			
			Collection<GrantedAuthority> authorities = user.getAuthorities();
			String role = StringUtils.collectionToCommaDelimitedString(authorities);
			
			logger.info("Updated user's Role = " + role);
			
			userInfo.setRole(role);

			Long updateResult = uds.updateUser(userInfo);
			
			result = new APIResponse();
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(updateResult);
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
