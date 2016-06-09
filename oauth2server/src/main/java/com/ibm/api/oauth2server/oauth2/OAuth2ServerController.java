package com.ibm.api.oauth2server.oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OAuth2ServerController
{

	@RequestMapping({ "/", "/index" })
	public String getIndex()
	{
		return "index";
	}
	
}
