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

public class TagDaoImpl implements TagDao {
	private SessionFactory sessionFactory;
	private Log Logger = LogFactory.getLog(AssetDaoImpl.class);

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
	public List<Tag> saveTags(List<Tag> tags) {
		List<Tag> tagsInDB = null;

		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			Logger.info("start of saveTags");
			tx = session.beginTransaction();
			Logger.info("transactionbegin");
			for (Tag tag : tags) {
				try {
					if (isNameUnique(session, tag.getName())) {
						session.save(tag);
					}
				} catch (HibernateException e) {
					if (tx != null) {
						tx.rollback();
					}
				}
			}
			tx.commit();

			Query<Tag> query = session.createQuery("from Tag where asset IS NULL", Tag.class);
			tagsInDB = query.getResultList();

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

		return tagsInDB;
	}

	@Override
	public Tag getTagByObservationId(Long observationId) {
		Tag tag = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Tag> query = session.createQuery("FROM Tag WHERE observation=:observation", Tag.class);
			query.setParameter("observation", observationId);
			tag = query.getResultList().get(0);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}

		return tag;
	}

	@Override
	public Tag getTagByName(String tagName) {
		Tag tag = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Tag> query = session.createQuery("FROM Tag WHERE name=:name", Tag.class);
			query.setParameter("name", tagName);
			tag = query.uniqueResult();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}

		return tag;
	}

	@Override
	public List<Tag> getTagsByNameSubString(String nameSubString) {
		nameSubString = nameSubString.toLowerCase();
		List<Tag> tags = null;
		Transaction tx = null;
		Session session = sessionFactory.openSession();
		try {
			tx = session.beginTransaction();
			Query<Tag> query = null;
			if (!nameSubString.isEmpty()) {
				query = session.createQuery("FROM Tag WHERE asset IS NULL AND LOWER(name) LIKE :nameSubString", Tag.class);
				query.setParameter("nameSubString", "%" + nameSubString + "%");
			} else {
				query = session.createQuery("from Tag where asset IS NULL", Tag.class);
			}
			tags = query.getResultList();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}

		return tags;
	}

	private boolean isNameUnique(Session session, String name) {
		Query<Long> query = session.createQuery("select count(*) from Tag where name = :name", Long.class);
		query.setParameter("name", name);
		Long count = query.uniqueResult();
		return count == 0; // ID is unique if count is 0
	}

}
