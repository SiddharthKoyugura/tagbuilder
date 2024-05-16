package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Asset extends PersistantObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String ecn;
	private Model model;
	private Lookup location;
	private Lookup assetCategory;
	private List<Asset> childAssets = new ArrayList<>();
	private List<Observation> observations = new ArrayList<>();

	private Boolean isCompleted = false;

	public Asset() {
	}

	@Override
	public void detach() {
		super.detach();
		childAssets = detachList(childAssets);
		observations = detachList(observations);
		if (model != null) {
			model.detach();
		}
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

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Lookup getLocation() {
		return location;
	}

	public void setLocation(Lookup location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Lookup getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(Lookup assetCategory) {
		this.assetCategory = assetCategory;
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

	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	@Override
	public String toString() {
		return "Asset [id=" + id + ", name=" + name + ", ecn=" + ecn + ", model=" + model + ", location=" + location
				+ ", assetCategory=" + assetCategory + ", childAssets=" + childAssets + ", observations=" + observations
				+ ", isCompleted=" + isCompleted + "]";
	}

}
