package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AssetServiceAsync {
	void saveAsset(Asset asset,AsyncCallback<Void> callback);
	void updateAsset(Asset asset, AsyncCallback<Void> callback);
	void saveAssets(List<Asset> assets, AsyncCallback<List<Asset>> callback);
	void getAssetById(String id, AsyncCallback<Asset> callback);
	void getAssetByName(String name, AsyncCallback<Asset> callback);
	void getParentAssets(AsyncCallback<List<Asset>> callback);
}
