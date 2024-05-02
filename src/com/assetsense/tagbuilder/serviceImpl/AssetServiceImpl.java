package com.assetsense.tagbuilder.serviceImpl;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.dao.AssetDao;
import com.assetsense.tagbuilder.service.AssetService;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class AssetServiceImpl extends RemoteServiceServlet implements AssetService{

	private AssetDao assetDao;
	
	@Override
	public void saveAsset(Asset asset) {
		assetDao = (AssetDao) ApplicationContextListener.applicationContext.getBean("assetDaoImpl");
		System.out.println("Hellloafsdfasdjfasdfads");
		assetDao.saveAsset(asset);
	}

	public AssetDao getAssetDao() {
		return assetDao;
	}

	public void setAssetDao(AssetDao assetDao) {
		this.assetDao = assetDao;
	}
	
}
