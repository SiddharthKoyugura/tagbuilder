package com.assetsense.tagbuilder.service;

import com.assetsense.tagbuilder.pi.domain.Element;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("element")
public interface ElementService extends RemoteService{
	
	public void saveElement(Element element) throws IllegalArgumentException;
	
}
