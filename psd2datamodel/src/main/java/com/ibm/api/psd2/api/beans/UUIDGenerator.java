package com.ibm.api.psd2.api.beans;

import java.util.UUID;

public class UUIDGenerator
{
	public static String generateUUID()
	{
		return UUID.randomUUID().toString();
	}

}
