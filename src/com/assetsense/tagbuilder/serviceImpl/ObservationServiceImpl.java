package com.assetsense.tagbuilder.serviceImpl;

import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.dao.ObservationDao;
import com.assetsense.tagbuilder.service.ObservationService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ObservationServiceImpl extends RemoteServiceServlet implements ObservationService{

	private ObservationDao observationDao;
	
	@Override
	public void saveObservation(Observation observation) throws IllegalArgumentException {
		observationDao = (ObservationDao) ApplicationContextListener.applicationContext.getBean("observationDaoImpl");
		observationDao.saveObservation(observation);
	}

	public ObservationDao getObservationDao() {
		return observationDao;
	}

	public void setObservationDao(ObservationDao observationDao) {
		this.observationDao = observationDao;
	}

}
