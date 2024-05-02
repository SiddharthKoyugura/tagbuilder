package com.assetsense.tagbuilder.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.dto.TagDTO;
import com.assetsense.tagbuilder.utils.TypeConverter;

public class TagDaoImpl implements TagDao {
	private SessionFactory sessionFactory;
	private Log Logger = LogFactory.getLog(AssetDaoImpl.class);
	private final TypeConverter typeConverter = new TypeConverter();

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void saveTag(Tag tag) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			session.save(tag);
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
	public void saveTags(List<Tag> tags) {
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			for (Tag tag : tags) {
				try {
					session.save(tag);
					tx.commit();
				} catch (Exception e) {
					if (tx != null) {
						tx.rollback();
					}
				}
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
		Logger.info("end of save tags");
	}

	@Override
	public TagDTO getTagByObservationId(Long observationId) {
		TagDTO tagDTO = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Tag> query = session.createQuery("FROM Tag WHERE observation=:observation", Tag.class);
			query.setParameter("observation", observationId);
			Tag tag = query.getResultList().get(0);
			tagDTO = typeConverter.convertToTagDTO(tag);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}

		return tagDTO;
	}

}
