package com.ibm.api.psd2.webclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PSD2ClientController
{

	@RequestMapping({ "/", "/index" })
	public String getIndex()
	{
		return "index";
	}

	@RequestMapping("/login")
	public String getLogin()
	{
		return "login";
	}
}
