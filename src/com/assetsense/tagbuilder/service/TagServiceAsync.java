package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Tag;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TagServiceAsync {

	void saveTag(Tag tag, AsyncCallback<Void> callback) throws IllegalArgumentException;

	void saveTags(List<Tag> tags, AsyncCallback<List<Tag>> callback);

	void getTagByObservationId(Long observationId, AsyncCallback<Tag> callback);
	
	void getTagByName(String tagName, AsyncCallback<Tag> callback);
	
	void getTagsByNameSubString(String nameSubString, AsyncCallback<List<Tag>> callback);
}
