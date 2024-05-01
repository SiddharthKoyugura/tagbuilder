package com.assetsense.tagbuilder.service;

import com.assetsense.tagbuilder.c2.domain.Tag;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TagServiceAsync {

	void saveTag(Tag tag,AsyncCallback<Void> callback) throws IllegalArgumentException;
}
