package com.assetsense.tagbuilder.dao;

import org.hibernate.SessionFactory;

import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.dao.ObservationDao;

public class ObservationDaoImpl implements ObservationDao {
	private SessionFactory sessionFactory;

	@Override
	public void saveObservation(Observation observation) {
		// TODO Auto-generated method stub
		
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
}
