package com.assetsense.tagbuilder.dao;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Tag;

public interface TagDao {

	public void saveTag(Tag tag);
	
	public List<Tag> saveTags(List<Tag> tags);
	
	public Tag getTagByObservationId(Long observationId);
	
	public Tag getTagByName(String tagName);
	
	public List<Tag> getTagsByNameSubString(String nameSubString);
}
