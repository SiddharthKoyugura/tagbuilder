package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.dto.AssetDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("asset")
public interface AssetService extends RemoteService{
	void saveAsset(Asset asset);
	void updateAsset(Asset asset);
	List<AssetDTO> saveAssets(List<Asset> assets);
	AssetDTO getAssetById(String id);
	AssetDTO getAssetByName(String name);
}
