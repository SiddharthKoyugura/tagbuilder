package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LookupServiceAsync {
 
	void getLookups(AsyncCallback<List<Lookup>> callback);
	 
	void getLookupByCategory(String category,AsyncCallback<Lookup> callback);
}
