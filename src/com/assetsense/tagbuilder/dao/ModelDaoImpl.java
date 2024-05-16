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
import com.assetsense.tagbuilder.c2.domain.Model;

public class ModelDaoImpl implements ModelDao {

	private Log Logger = LogFactory.getLog(ModelDaoImpl.class);
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Model> getModelsByAssetCategoryName(String name) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		List<Model> models = null;
		try {
			Logger.info("Transaction begin");
			tx = session.beginTransaction();
			Query<Lookup> query = (Query<Lookup>) session.createQuery("from Lookup where name=:name", Lookup.class);
			query.setParameter("name", name);
			if (query.getResultList().size() > 0) {
				Lookup lookup = query.getResultList().get(0);

				Query<Model> modelQuery = (Query<Model>) session.createQuery("from Model where asset_category=:id",
						Model.class);
				modelQuery.setParameter("id", lookup.getId());
				models = modelQuery.getResultList();
			}
			tx.commit();
			Logger.info("Transaction End");
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return models;
	}

	@Override
	public Model saveModel(Model model) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		Model modelInDb = null;
		try {
			Logger.info("Transaction begin");
			tx = session.beginTransaction();
			session.save(model);

			Query<Model> query = (Query<Model>) session.createQuery("from Model where name=:name", Model.class);
			query.setParameter("name", model.getName());
			if (query.getResultList().size() > 0) {
				modelInDb = query.getResultList().get(0);
			}

			tx.commit();
			Logger.info("Transaction End");
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return modelInDb;
	}

	@Override
	public Model getModelByName(String modelName) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		Model model = null;
		try {
			tx = session.beginTransaction();
			Query<Model> query = (Query<Model>) session.createQuery("from Model where name=:name", Model.class);
			query.setParameter("name", modelName);
			if (query.getResultList().size() > 0) {
				model = query.getResultList().get(0);
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return model;
	}

}
