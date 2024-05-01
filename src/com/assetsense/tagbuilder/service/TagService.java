package com.assetsense.tagbuilder.service;

import com.assetsense.tagbuilder.c2.domain.Tag;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tag")
public interface TagService extends RemoteService{

	public void saveTag(Tag tag) throws IllegalArgumentException;
}
