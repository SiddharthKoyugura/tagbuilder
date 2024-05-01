package com.assetsense.tagbuilder.service;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AssetServiceAsync {
	
	void saveAsset(Asset asset,AsyncCallback<Void> callback);
}
