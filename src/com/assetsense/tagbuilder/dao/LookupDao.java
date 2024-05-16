package com.assetsense.tagbuilder.dao;

import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.assetsense.tagbuilder.c2.domain.Measurement;

public interface LookupDao {

	List<Lookup> getLookups();

	List<Lookup> getLookupByCategory(String category);

	List<Measurement> getMeasurements();

	Lookup saveLookup(Lookup lookup);
	
	Lookup getLookupByName(String name);
	
	List<Lookup> getLookupsByNames(List<String> names);
	
	List<Lookup> getLookupByMeasurementName(String measurement);
	
	Measurement getMeasurementByUnitId(String unitId);
}
