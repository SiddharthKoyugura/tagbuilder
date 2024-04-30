package com.assetsense.tagbuilder.dao;

import org.hibernate.SessionFactory;

import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.dao.TagDao;

public class TagDaoImpl implements TagDao {
	private SessionFactory sessionFactory;

	@Override
	public void saveTag(Tag tag) {
		// TODO Auto-generated method stub
		
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
}
