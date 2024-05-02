package com.assetsense.tagbuilder.dao;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.assetsense.tagbuilder.c2.domain.Measurement;

public interface LookupDao {

	List<Lookup> getLookups();

	Lookup getLookupByCategory(String category);

	List<Measurement> getMeasurements();

}
