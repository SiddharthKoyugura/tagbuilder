package com.assetsense.tagbuilder.dao;

import com.assetsense.tagbuilder.c2.domain.Asset;

public interface MigrationDao {
	void saveAsset(Asset asset);
}
