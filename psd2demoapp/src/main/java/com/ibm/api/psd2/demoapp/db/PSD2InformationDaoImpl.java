package com.ibm.api.psd2.demoapp.db;

import static com.mongodb.client.model.Filters.eq;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.api.psd2.demoapp.beans.PSD2Bookmark;
import com.ibm.api.psd2.demoapp.beans.PSD2Information;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Component
public class PSD2InformationDaoImpl implements PSD2InformationDao
{
	private static final Logger logger = LogManager.getLogger(PSD2InformationDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.info}")
	private String infoCollection;

	@Value("${mongodb.collection.bookmarks}")
	private String bookmarksColl;

	@Override
	public PSD2Information getInfo(String tag) throws Exception
	{
		PSD2Information info = null;

		if (!TAG_BOOKMARKS.equals(tag))
		{
			MongoCollection<Document> coll = conn.getDB().getCollection(infoCollection);
			FindIterable<Document> iterable = coll.find(eq("tag", tag));

			for (Document document : iterable)
			{
				if (document != null)
				{
					logger.info("message = " + document.toJson());
					info = mdp.parse(document, new PSD2Information());
				}
			}
		}
		else
		{
			MongoCollection<Document> coll = conn.getDB().getCollection(bookmarksColl);
			FindIterable<Document> iterable = coll.find();
			Set<PSD2Bookmark> psd2bookmarks = new HashSet<>();
			
			for (Document document : iterable)
			{
				if (document != null)
				{
					logger.info("bookmark = " + document.toJson());
					PSD2Bookmark pb = mdp.parse(document, new PSD2Bookmark());
					psd2bookmarks.add(pb);
				}
			}
			
			info = new PSD2Information();
			info.setTag(TAG_BOOKMARKS);
			info.setAdditionalInfo(psd2bookmarks);
		}
		return info;
	}

}
