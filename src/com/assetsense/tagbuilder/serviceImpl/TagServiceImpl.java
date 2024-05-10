package com.assetsense.tagbuilder.serviceImpl;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.dao.TagDaoImpl;
import com.assetsense.tagbuilder.service.TagService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TagServiceImpl extends RemoteServiceServlet implements TagService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TagDaoImpl tagDao;

	public TagDaoImpl getTagDao() {
		return tagDao;
	}

	public void setTagDao(TagDaoImpl tagDao) {
		this.tagDao = tagDao;
	}

	@Override
	public void saveTag(Tag tag) throws IllegalArgumentException {
		tagDao = (TagDaoImpl) ApplicationContextListener.applicationContext.getBean("tagDaoImpl");
		tagDao.saveTag(tag);
	}

	@Override
	public List<Tag> saveTags(List<Tag> tags) {
		tagDao = (TagDaoImpl) ApplicationContextListener.applicationContext.getBean("tagDaoImpl");
		List<Tag> tagsInDB = tagDao.saveTags(tags);
		for (Tag tag : tagsInDB) {
			tag.detach();
			if (tag.getObservation() != null) {
				tag.getObservation().detach();
			}
			if (tag.getAsset() != null) {
				tag.getAsset().detach();
			}
		}
		return tagsInDB;
	}

	@Override
	public Tag getTagByObservationId(Long observationId) {
		tagDao = (TagDaoImpl) ApplicationContextListener.applicationContext.getBean("tagDaoImpl");
		return tagDao.getTagByObservationId(observationId);
	}

	@Override
	public Tag getTagByName(String tagName) {
		tagDao = (TagDaoImpl) ApplicationContextListener.applicationContext.getBean("tagDaoImpl");
		return tagDao.getTagByName(tagName);
	}

	@Override
	public List<Tag> getTagsByNameSubString(String nameSubString) {
		tagDao = (TagDaoImpl) ApplicationContextListener.applicationContext.getBean("tagDaoImpl");
		List<Tag> tags = tagDao.getTagsByNameSubString(nameSubString);
		for (Tag tag : tags) {
			tag.detach();
			if (tag.getObservation() != null) {
				tag.getObservation().detach();
			}
			if (tag.getAsset() != null) {
				tag.getAsset().detach();
			}
		}
		return tags;
	}

	@Override
	public List<Tag> getTagsByNames(List<String> names) {
		tagDao = (TagDaoImpl) ApplicationContextListener.applicationContext.getBean("tagDaoImpl");
		List<Tag> tags = tagDao.getTagsByNames(names);
		for (Tag tag : tags) {
			tag.detach();
			if (tag.getObservation() != null) {
				tag.getObservation().detach();
			}
			if (tag.getAsset() != null) {
				tag.getAsset().detach();
			}
		}
		return tags;
	}

}
