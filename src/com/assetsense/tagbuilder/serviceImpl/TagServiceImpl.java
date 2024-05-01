package com.assetsense.tagbuilder.serviceImpl;

import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.dao.TagDao;
import com.assetsense.tagbuilder.service.TagService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TagServiceImpl extends RemoteServiceServlet implements TagService {

	private TagDao tagDao;
	
	@Override
	public void saveTag(Tag tag) throws IllegalArgumentException {
		tagDao = (TagDao) ApplicationContextListener.applicationContext.getBean("TagDaoImpl");
		tagDao.saveTag(tag);
	}

	public TagDao getTagDao() {
		return tagDao;
	}

	public void setTagDao(TagDao tagDao) {
		this.tagDao = tagDao;
	}

	
}
