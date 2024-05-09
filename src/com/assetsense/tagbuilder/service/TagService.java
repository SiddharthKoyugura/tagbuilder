package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Tag;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tag")
public interface TagService extends RemoteService{

	public void saveTag(Tag tag) throws IllegalArgumentException;
	public List<Tag> saveTags(List<Tag> tags);
	public Tag getTagByObservationId(Long observationId);
	public Tag getTagByName(String tagName);
	public List<Tag> getTagsByNameSubString(String nameSubString);
}
