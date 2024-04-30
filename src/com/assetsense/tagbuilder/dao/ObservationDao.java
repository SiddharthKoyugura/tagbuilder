package com.assetsense.tagbuilder.dao;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.c2.domain.Observation;

public interface ObservationDao {

	public void saveObservation(Observation observation);
}
