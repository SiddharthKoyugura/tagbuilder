package com.assetsense.tagbuilder.dao;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.dto.AssetDTO;

public interface AssetDao {

	public void saveAsset(Asset asset);
	public void updateAsset(Asset asset);
	public List<AssetDTO> saveAssets(List<Asset> assets);
	public AssetDTO getAssetById(String id);
	public AssetDTO getAssetByName(String name);
	public List<AssetDTO> getParentAssets();
}
