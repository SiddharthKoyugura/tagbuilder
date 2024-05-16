package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Model;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ModelServiceAsync {
	void getModelsByAssetCategoryName(String name, AsyncCallback<List<Model>> callback);
	
	void saveModel(Model model, AsyncCallback<Model> callback);
	
	void getModelByName(String modelName, AsyncCallback<Model> callback);
}
