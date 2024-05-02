package com.assetsense.tagbuilder.dao;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Lookup;

public interface LookupDao {

	List<Lookup> getLookups();

	Lookup getLookupByCategory(String category);

}
