package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.dto.AssetDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AssetServiceAsync {
	void saveAsset(Asset asset,AsyncCallback<Void> callback);
	void updateAsset(Asset asset, AsyncCallback<Void> callback);
	void saveAssets(List<Asset> assets, AsyncCallback<List<AssetDTO>> callback);
	void getAssetById(String id, AsyncCallback<AssetDTO> callback);
	void getAssetByName(String name, AsyncCallback<AssetDTO> callback);
}
