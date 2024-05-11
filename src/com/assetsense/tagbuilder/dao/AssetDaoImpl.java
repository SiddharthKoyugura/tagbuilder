package com.assetsense.tagbuilder.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.assetsense.tagbuilder.c2.domain.Asset;

public class AssetDaoImpl implements AssetDao {

	private Log Logger = LogFactory.getLog(AssetDaoImpl.class);
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void saveAsset(Asset asset) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			if(asset.getId() == null){
				asset.setId(UUID.randomUUID().toString());
			}
			if (!isIdUnique(session, asset.getId())) {
				session.update(asset);
			} else {
				session.save(asset);
			}
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
	}

	@Override
	public void updateAsset(Asset asset) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			session.update(asset);
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
	}

	@Override
	public List<Asset> saveAssets(List<Asset> assets) {
		List<Asset> assetsInDb = new ArrayList<>();
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			for (Asset asset : assets) {
				try {
					if (isIdUnique(session, asset.getId())) {
						session.save(asset);
					}
				} catch (Exception e) {
					if (tx != null) {
						tx.rollback();
					}
				}
			}
			tx.commit();
			Query<Asset> query = session.createQuery("from Asset where is_completed=false AND parent_id IS NULL",
					Asset.class);
			assetsInDb = query.getResultList();
			Logger.info("transcation completed");
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		Logger.info("end of save Assets");
		return assetsInDb;
	}

	@Override
	public Asset getAssetById(String id) {
		Asset asset = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			asset = session.get(Asset.class, id);
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
		return asset;
	}

	private boolean isIdUnique(Session session, String id) {
		Query<Long> query = session.createQuery("select count(*) from Asset where id = :id", Long.class);
		query.setParameter("id", id);
		Long count = query.uniqueResult();
		return count == 0; // ID is unique if count is 0
	}

	@Override
	public Asset getAssetByName(String name) {
		Asset asset = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			Query<Asset> query = session.createQuery("FROM Asset Where name=:name", Asset.class);
			query.setParameter("name", name);
			asset = query.getResultList().get(0);
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
		return asset;

	}

	@Override
	public List<Asset> getParentAssets() {
		List<Asset> assets = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			Query<Asset> query = session.createQuery("from Asset where is_completed=false AND parent_id IS NULL",
					Asset.class);
			assets = query.getResultList();
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
		Logger.info("end of get Assets");
		return assets;
	}

}
