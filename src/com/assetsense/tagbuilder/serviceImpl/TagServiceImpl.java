package com.assetsense.tagbuilder.serviceImpl;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.dao.TagDaoImpl;
import com.assetsense.tagbuilder.dto.TagDTO;
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
	public void saveTags(List<Tag> tags) {
		tagDao = (TagDaoImpl) ApplicationContextListener.applicationContext.getBean("tagDaoImpl");
		tagDao.saveTags(tags);
	}

	@Override
	public TagDTO getTagByObservationId(Long observationId) {
		tagDao = (TagDaoImpl) ApplicationContextListener.applicationContext.getBean("tagDaoImpl");
		return tagDao.getTagByObservationId(observationId);
	}

}
