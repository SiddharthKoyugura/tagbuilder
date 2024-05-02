package com.assetsense.tagbuilder.dto;

import java.io.Serializable;

import com.assetsense.tagbuilder.c2.domain.Lookup;

public class TagDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private AssetDTO asset;
	private ObservationDTO observation;
	private Lookup category;

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

	public AssetDTO getAsset() {
		return asset;
	}

	public void setAsset(AssetDTO asset) {
		this.asset = asset;
	}

	public ObservationDTO getObservation() {
		return observation;
	}

	public void setObservation(ObservationDTO observation) {
		this.observation = observation;
	}

	public Lookup getCategory() {
		return category;
	}

	public void setCategory(Lookup category) {
		this.category = category;
	}

}
