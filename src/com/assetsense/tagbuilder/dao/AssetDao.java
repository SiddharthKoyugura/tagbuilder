package com.assetsense.tagbuilder.dao;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Asset;

public interface AssetDao {

	public void saveAsset(Asset asset);
	public void updateAsset(Asset asset);
	public List<Asset> saveAssets(List<Asset> assets);
	public Asset getAssetById(String id);
	public Asset getAssetByName(String name);
	public List<Asset> getParentAssets();
}
