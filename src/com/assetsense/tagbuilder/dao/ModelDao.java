package com.assetsense.tagbuilder.dao;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Model;

public interface ModelDao {
	List<Model> getModelsByAssetCategoryName(String name);
	
	Model saveModel(Model model);
	
	Model getModelByName(String modelName);
}
