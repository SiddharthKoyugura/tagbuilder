package com.assetsense.tagbuilder.service;

import com.assetsense.tagbuilder.pi.domain.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ElementServiceAsync {
	
	void saveElement(Element element,AsyncCallback<Void> callback);
}
