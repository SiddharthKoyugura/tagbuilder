package com.assetsense.tagbuilder.serviceImpl;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.dao.AssetDaoImpl;
import com.assetsense.tagbuilder.service.AssetService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class AssetServiceImpl extends RemoteServiceServlet implements AssetService {

	private AssetDaoImpl assetDao;

	public AssetDaoImpl getAssetDao() {
		return assetDao;
	}

	public void setAssetDao(AssetDaoImpl assetDao) {
		this.assetDao = assetDao;
	}

	@Override
	public void saveAsset(Asset asset) {
		assetDao = (AssetDaoImpl) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		assetDao.saveAsset(asset);
	}

	@Override
	public List<Asset> saveAssets(List<Asset> assets) {
		assetDao = (AssetDaoImpl) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		List<Asset> assetsInDB =  assetDao.saveAssets(assets);
		for(Asset asset: assetsInDB){
			asset.detach();
		}
		return assetsInDB;
	}

	@Override
	public Asset getAssetById(String id) {
		assetDao = (AssetDaoImpl) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		Asset asset = assetDao.getAssetById(id);
		asset.detach();
		return asset;
	}

	@Override
	public Asset getAssetByName(String name) {
		assetDao = (AssetDaoImpl) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		Asset asset = assetDao.getAssetByName(name);
		asset.detach();
		return asset;
	}

	@Override
	public void updateAsset(Asset asset) {
		assetDao = (AssetDaoImpl) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		assetDao.updateAsset(asset);
	}

	@Override
	public List<Asset> getParentAssets() {
		assetDao = (AssetDaoImpl) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		List<Asset> assets =  assetDao.getParentAssets();
		for(Asset asset: assets){
			asset.detach();
		}
		return assets;
	}

}
