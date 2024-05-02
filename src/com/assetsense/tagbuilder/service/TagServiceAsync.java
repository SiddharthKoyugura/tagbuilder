package com.assetsense.tagbuilder.service;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.c2.domain.Tag;
import com.assetsense.tagbuilder.dto.TagDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TagServiceAsync {

	void saveTag(Tag tag, AsyncCallback<Void> callback) throws IllegalArgumentException;

	void saveTags(List<Tag> tags, AsyncCallback<Void> callback);

	void getTagByObservationId(Long observationId, AsyncCallback<TagDTO> callback);
}
