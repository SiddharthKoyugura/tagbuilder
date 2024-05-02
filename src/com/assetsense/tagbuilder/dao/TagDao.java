package com.assetsense.tagbuilder.dao;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.dto.TagDTO;

public interface TagDao {

	public void saveTag(Tag tag);
	
	public void saveTags(List<Tag> tags);
	
	public TagDTO getTagByObservationId(Long observationId);
}
