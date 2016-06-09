package com.ibm.api.psd2.demoapp.db;

import com.ibm.api.psd2.demoapp.beans.PSD2Information;

public interface PSD2InformationDao
{
	public static final String TAG_GENERAL="general";
	public static final String TAG_IBM="ibm";
	public static final String TAG_BOOKMARKS = "bookmarks";
	
	public PSD2Information getInfo(String tag) throws Exception;
}
