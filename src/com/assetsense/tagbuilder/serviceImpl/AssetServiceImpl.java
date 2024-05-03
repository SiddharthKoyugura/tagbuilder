package com.assetsense.tagbuilder.serviceImpl;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.dao.AssetDaoImpl;
import com.assetsense.tagbuilder.dto.AssetDTO;
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
	public List<AssetDTO> saveAssets(List<Asset> assets) {
		assetDao = (AssetDaoImpl) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		return assetDao.saveAssets(assets);
	}

	@Override
	public AssetDTO getAssetById(String id) {
		assetDao = (AssetDaoImpl) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		return assetDao.getAssetById(id);
	}

	@Override
	public AssetDTO getAssetByName(String name) {
		assetDao = (AssetDaoImpl) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		return assetDao.getAssetByName(name);
	}

	@Override
	public void updateAsset(Asset asset) {
		assetDao = (AssetDaoImpl) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		assetDao.updateAsset(asset);
	}

}
