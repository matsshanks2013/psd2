package com.ibm.api.oauth2server.db;

import static com.mongodb.client.model.Filters.eq;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ibm.api.oauth2server.beans.UserInfo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

	private static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.users}")
	private String collection;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		
		UserInfo userInfo = null;
		
		try
		{
			userInfo = getUserInfo(username);
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
		
		GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getRole());
		UserDetails userDetails = (UserDetails)new User(userInfo.getUsername(), userInfo.getPassword(), Arrays.asList(authority));

		return userDetails;
	}

	private UserInfo getUserInfo(String username) throws Exception
	{
		MongoCollection<Document> coll = conn.getDB().getCollection(collection);
		FindIterable<Document> iterable = coll.find(eq("username", username));

		UserInfo userInfo= null;

		for (Document document : iterable)
		{
			if (document != null)
			{
				logger.info("message = " + document.toJson());
				userInfo = mdp.parse(document, new UserInfo());
			}
		}
		return userInfo;
	}
	
	public void createUserInfo(UserInfo u) throws Exception
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		u.setPassword(encoder.encode(u.getPassword()));
		MongoCollection<Document> coll = conn.getDB().getCollection(collection);
		coll.insertOne(mdp.format(u));
	}

}
