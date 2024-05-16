package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Model;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("model")
public interface ModelService extends RemoteService {
	List<Model> getModelsByAssetCategoryName(String name);
	Model saveModel(Model model);
	Model getModelByName(String modelName);
}
