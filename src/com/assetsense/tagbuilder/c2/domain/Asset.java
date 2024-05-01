package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Asset implements Serializable {

	/**
	 *
	 */

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String ecn;
	private String model;
	private String location;
	private Lookup Assettype;
	private Lookup supplierName;

	private String category;
	private List<Asset> childAssets = new ArrayList<>();
	private List<Observation> observations = new ArrayList<>();

	public Asset() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEcn() {
		return ecn;
	}

	public List<Observation> getObservations() {
		return observations;
	}

	public void setObservations(List<Observation> observations) {
		this.observations = observations;
	}

	public void setEcn(String ecn) {
		this.ecn = ecn;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Asset> getChildAssets() {
		return childAssets;
	}

	public void setChildAssets(List<Asset> childAssets) {
		this.childAssets = childAssets;
	}

	public List<Observation> getAttribute() {
		return observations;
	}

	public void setAttribute(List<Observation> observations) {
		this.observations = observations;
	}

}
