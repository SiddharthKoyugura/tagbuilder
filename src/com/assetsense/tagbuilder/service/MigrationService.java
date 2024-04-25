package com.assetsense.tagbuilder.service;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("migration")
public interface MigrationService extends RemoteService {
	void saveAsset(Asset asset);
}
