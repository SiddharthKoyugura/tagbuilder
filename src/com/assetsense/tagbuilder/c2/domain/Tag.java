package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;

public class Tag extends PersistantObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private Asset asset;
	private Observation observation;
	private Lookup category;

	public Tag() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Observation getObservation() {
		return observation;
	}

	public void setObservation(Observation observation) {
		this.observation = observation;
	}

	public Lookup getCategory() {
		return category;
	}

	public void setCategory(Lookup category) {
		this.category = category;
	}

}
