package com.assetsense.tagbuilder.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.dao.AssetDao;
import com.assetsense.tagbuilder.dto.AssetDTO;
import com.assetsense.tagbuilder.utils.TypeConverter;

public class AssetDaoImpl implements AssetDao {
	private final TypeConverter typeConverter = new TypeConverter();

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
			session.save(asset);
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
	public List<AssetDTO> saveAssets(List<Asset> assets) {
		List<AssetDTO> assetDTOs = new ArrayList<>();
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
			List<Asset> assetsInDb = query.getResultList();
			for (Asset asset : assetsInDb) {
				assetDTOs.add(typeConverter.convertToAssetDTO(asset));
			}
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
		return assetDTOs;
	}

	@Override
	public AssetDTO getAssetById(String id) {
		AssetDTO asset = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			Asset assetInDB = session.get(Asset.class, id);
			asset = typeConverter.convertToAssetDTO(assetInDB);
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

}
