package com.ibm.api.psd2.demoapp.db;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.validator.routines.EmailValidator;
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
import org.springframework.util.StringUtils;

import com.ibm.api.psd2.demoapp.beans.UserInfo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

	private static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.users}")
	private String usersColl;
	
	@Autowired
	private BCryptPasswordEncoder bcpe;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		logger.info("Loading User Details: " + username);
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
		
		Set<String> auths = StringUtils.commaDelimitedListToSet(userInfo.getRole());
		ArrayList<GrantedAuthority> authorities =new ArrayList<>(); 
		
		for (Iterator<String> iterator = auths.iterator(); iterator.hasNext();)
		{
			String auth = iterator.next();
			GrantedAuthority authority = new SimpleGrantedAuthority(auth);
			authorities.add(authority);
		}

		UserDetails userDetails = (UserDetails)new User(userInfo.getEmail(), userInfo.getPassword(), authorities);
		return userDetails;
	}

	public UserInfo getUserInfo(String username) throws Exception
	{
		MongoCollection<Document> coll = conn.getDB().getCollection(usersColl);
		FindIterable<Document> iterable = coll.find(eq("email", username)).projection(excludeId());

		UserInfo userInfo= null;

		for (Document document : iterable)
		{
			if (document != null)
			{
				logger.info("userInfo = " + document.toJson());
				userInfo = mdp.parse(document, new UserInfo());
			}
		}
		return userInfo;
	}
	
	private boolean validateEmail(String email)
	{
		EmailValidator ev = EmailValidator.getInstance();
		return ev.isValid(email);
	}
	
	private void validateUser(UserInfo userInfo)
	{
		if (userInfo == null)
		{
			throw new IllegalArgumentException("UserInfo Object is null");
		}
		if (StringUtils.isEmpty(userInfo.getFirstName()))
		{
			throw new IllegalArgumentException("First Name can't be null");
		}
		if (StringUtils.isEmpty(userInfo.getLastName()))
		{
			throw new IllegalArgumentException("Last Name can't be null");
		}
		if (StringUtils.isEmpty(userInfo.getPassword()))
		{
			throw new IllegalArgumentException("Password can't be null or empty");
		}
		if (StringUtils.isEmpty(userInfo.getPhone()))
		{
			throw new IllegalArgumentException("Phone can't be null or empty");
		}
		if(!validateEmail(userInfo.getEmail()))
		{
			throw new IllegalArgumentException("Username is not a valid email addresses");
		}
	}

	public void addUser(UserInfo userInfo) throws Exception
	{
		validateUser(userInfo);
		
		if (getUserInfo(userInfo.getEmail()) != null)
		{
			throw new IllegalArgumentException("User Already Exists");
		}
		logger.info("Inserting User: " + userInfo.getEmail());
		userInfo.setPassword(bcpe.encode(userInfo.getPassword()));
		MongoCollection<Document> coll = conn.getDB().getCollection(usersColl);
		coll.insertOne(mdp.format(userInfo));
	}
	
	public long updateUser(UserInfo userInfo) throws Exception
	{
		validateUser(userInfo);
		logger.info("updating user: " + userInfo.getEmail());
		
		userInfo.setPassword(bcpe.encode(userInfo.getPassword()));
		
		MongoCollection<Document> coll = conn.getDB().getCollection(usersColl);
		UpdateResult ur = coll.replaceOne(new Document("email", userInfo.getEmail()), mdp.format(userInfo));
		return ur.getModifiedCount();
	}

}
