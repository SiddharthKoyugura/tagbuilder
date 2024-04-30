package com.assetsense.tagbuilder.dao;

import org.hibernate.SessionFactory;

import com.assetsense.tagbuilder.dao.ElementDao;
import com.assetsense.tagbuilder.pi.domain.Element;

public class ElementDaoImpl implements ElementDao{

	private SessionFactory sessionFactory;
	
	@Override
	public void saveElement(Element element) {
		// TODO Auto-generated method stub
		
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	

}
