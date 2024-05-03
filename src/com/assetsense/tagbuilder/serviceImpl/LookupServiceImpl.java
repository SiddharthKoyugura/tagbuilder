package com.assetsense.tagbuilder.serviceImpl;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.assetsense.tagbuilder.c2.domain.Measurement;
import com.assetsense.tagbuilder.dao.LookupDao;
import com.assetsense.tagbuilder.service.LookupService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LookupServiceImpl  extends RemoteServiceServlet implements LookupService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LookupDao lookupDao;
	
	@Override
	public List<Lookup> getLookups() throws IllegalArgumentException {
		lookupDao = (LookupDao) ApplicationContextListener.applicationContext.getBean("lookupDaoImpl");
		return lookupDao.getLookups();
	}

	@Override
	public List<Lookup> getLookupByCategory(String category) throws IllegalArgumentException {
		lookupDao = (LookupDao) ApplicationContextListener.applicationContext.getBean("lookupDaoImpl");
		return lookupDao.getLookupByCategory(category);
	}

	public LookupDao getLookupDao() {
		return lookupDao;
	}

	public void setLookupDao(LookupDao lookupDao) {
		this.lookupDao = lookupDao;
	}

	@Override
	public List<Measurement> getMeasurements() throws IllegalArgumentException {
		lookupDao = (LookupDao) ApplicationContextListener.applicationContext.getBean("lookupDaoImpl");
		return lookupDao.getMeasurements();
	}

	@Override
	public void saveLookup(Lookup lookup) {
		lookupDao = (LookupDao) ApplicationContextListener.applicationContext.getBean("lookupDaoImpl");
		lookupDao.saveLookup(lookup);
	}

	@Override
	public Lookup getLookupByName(String name) {
		lookupDao = (LookupDao) ApplicationContextListener.applicationContext.getBean("lookupDaoImpl");
		return lookupDao.getLookupByName(name);
	}

	@Override
	public List<Lookup> getLookupByMeasurementName(String measurement) {
		lookupDao = (LookupDao) ApplicationContextListener.applicationContext.getBean("lookupDaoImpl");
		return lookupDao.getLookupByMeasurementName(measurement);
	}
	
}
