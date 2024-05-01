package com.assetsense.tagbuilder.service;

import com.assetsense.tagbuilder.c2.domain.Observation;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ObservationServiceAsync {

	void saveObservation(Observation observation,AsyncCallback<Void> callback) throws IllegalArgumentException;
}
