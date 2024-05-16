package com.assetsense.tagbuilder.serviceImpl;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Model;
import com.assetsense.tagbuilder.dao.ModelDaoImpl;
import com.assetsense.tagbuilder.service.ModelService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ModelServiceImpl extends RemoteServiceServlet implements ModelService {

	private ModelDaoImpl modelDao;

	public ModelDaoImpl getModelDao() {
		return modelDao;
	}

	public void setModelDao(ModelDaoImpl modelDao) {
		this.modelDao = modelDao;
	}

	@Override
	public List<Model> getModelsByAssetCategoryName(String name) {
		modelDao = (ModelDaoImpl) ApplicationContextListener.applicationContext.getBean("modelDaoImpl");
		List<Model> models = modelDao.getModelsByAssetCategoryName(name);
		for(Model model: models){
			model.detach();
		}
		return models;
	}

	@Override
	public Model saveModel(Model model) {
		modelDao = (ModelDaoImpl) ApplicationContextListener.applicationContext.getBean("modelDaoImpl");
		return modelDao.saveModel(model);
	}

	@Override
	public Model getModelByName(String modelName) {
		modelDao = (ModelDaoImpl) ApplicationContextListener.applicationContext.getBean("modelDaoImpl");
		return modelDao.getModelByName(modelName);
	}

}
