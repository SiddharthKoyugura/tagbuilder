package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("asset")
public interface AssetService extends RemoteService{
	void saveAsset(Asset asset);
	void updateAsset(Asset asset);
	List<Asset> saveAssets(List<Asset> assets);
	Asset getAssetById(String id);
	Asset getAssetByName(String name);
	List<Asset> getParentAssets();
}
