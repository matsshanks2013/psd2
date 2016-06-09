package com.ibm.psd2.integration.dao;

import java.util.Map;

public interface MongoDao
{
	public <T> boolean persist(T t) throws Exception;
	public <T> T findOneByAll(Map<String, Object> criteria, T t) throws Exception;
	public long update (String id, Object idValue, String key, Object value) throws Exception;


}
