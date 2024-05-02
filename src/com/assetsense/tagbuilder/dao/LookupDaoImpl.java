package com.assetsense.tagbuilder.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.assetsense.tagbuilder.c2.domain.Measurement;

public class LookupDaoImpl implements LookupDao{

	private Log Logger = LogFactory.getLog(AssetDaoImpl.class);
	private SessionFactory sessionFactory;
	
	@Override
	public List<Lookup> getLookups() {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		List<Lookup> lookups = null;
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			 Query<Lookup> query = session.createQuery("FROM Lookup", Lookup.class);
			 lookups = query.getResultList();
			tx.commit();
			Logger.info("transcation completed");
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		Logger.info("end of save Asset");
		
		return lookups;
	}

	@Override
	public Lookup getLookupByCategory(String category) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		Lookup lookup = null;
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			lookup = session.get(Lookup.class, category);
			tx.commit();
			Logger.info("transcation completed");
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		Logger.info("end of save Asset");
		
		return lookup;
	
	}
	
	@Override
	public List<Measurement> getMeasurements() {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		List<Measurement> measurement = null;
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			 Query<Measurement> query = session.createQuery("FROM Measurement", Measurement.class);
			 measurement = query.getResultList();
			tx.commit();
			Logger.info("transcation completed");
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		Logger.info("end of save Asset");
		
		return measurement;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

}
