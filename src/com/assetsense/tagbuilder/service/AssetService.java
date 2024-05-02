package com.assetsense.tagbuilder.service;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("asset")
public interface AssetService extends RemoteService{

	public void saveAsset(Asset asset) throws IllegalArgumentException;
}
