package com.assetsense.tagbuilder.service;

import com.assetsense.tagbuilder.c2.domain.Observation;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("observation")
public interface ObservationService extends RemoteService{

	public void saveObservation(Observation observation) throws IllegalArgumentException; 
}
