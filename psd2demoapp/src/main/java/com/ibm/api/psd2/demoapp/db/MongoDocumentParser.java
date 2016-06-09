package com.ibm.api.psd2.demoapp.db;

import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Component
public class MongoDocumentParser
{
	
	private static final Logger logger = LogManager.getLogger(MongoDocumentParser.class);


	public <T> T parse(Document document, T t) throws Exception
	{
		Set<Entry<String, Object>> valueSet = document.entrySet();
		
		for (Entry<String, Object> entry : valueSet)
		{
			String key = entry.getKey();
			Object value = entry.getValue();
//			logger.info("key = " + key + " value = " + value);
			key = Character.toUpperCase(key.charAt(0)) + key.substring(1);
//			logger.info("method name = set" + key);
			
			if (value != null)
			{
				try
				{
					Method m = t.getClass().getMethod("set" + key, value.getClass());
					m.invoke(t, value);
				}
				catch (NoSuchMethodException e)
				{
					logger.warn("no setter found for field: " + key + "Available methods are: " + t.getClass().getMethods());
				}
			}
		}
		return t;
	}
	
	public <T> Document format(T t) throws Exception
	{		
		Document document = new Document();
		Method[] methods = t.getClass().getMethods();
//		logger.info("methods = " + methods.length);
			
		for (Method method : methods)
		{
//			logger.info("analyzing method = " + method.getName());
//			logger.info(method.getName().startsWith("get"));
//			logger.info(method.getName().equals("getClass"));
//			logger.info(method.getParameterTypes().length != 0);
			if(method.getName().startsWith("get") && !method.getName().equals("getClass") && method.getParameterTypes().length == 0)
			{
				String key = method.getName().substring(3);
				key = Character.toLowerCase(key.charAt(0)) + key.substring(1);
//				logger.info("key = " + key);
				Object value = method.invoke(t);
				if (value != null)
				{
					document.append(key, value);
				}
			}
		}
		return document;
	}
}
