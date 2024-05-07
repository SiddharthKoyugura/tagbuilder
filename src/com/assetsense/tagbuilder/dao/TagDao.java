package com.assetsense.tagbuilder.dao;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Tag;

public interface TagDao {

	public void saveTag(Tag tag);
	
	public void saveTags(List<Tag> tags);
	
	public Tag getTagByObservationId(Long observationId);
}
