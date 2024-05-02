package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.assetsense.tagbuilder.c2.domain.Measurement;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("lookup")
public interface LookupService extends RemoteService{

	public List<Lookup> getLookups() throws IllegalArgumentException;
	
	public Lookup getLookupByCategory(String category) throws IllegalArgumentException;
	
	public List<Measurement> getMeasurements() throws IllegalArgumentException;
	
}
