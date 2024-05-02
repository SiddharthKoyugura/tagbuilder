package com.assetsense.tagbuilder.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Lookup;

public class AssetDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String ecn;
	private Lookup model;
	private String location;
	private Lookup assettype;
	private Lookup supplierName;

	private String category;
	private List<AssetDTO> childAssets = new ArrayList<>();
	private List<ObservationDTO> observations = new ArrayList<>();

	private Boolean isCompleted = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEcn() {
		return ecn;
	}

	public void setEcn(String ecn) {
		this.ecn = ecn;
	}

	public Lookup getModel() {
		return model;
	}

	public void setModel(Lookup model) {
		this.model = model;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Lookup getAssettype() {
		return assettype;
	}

	public void setAssettype(Lookup assettype) {
		this.assettype = assettype;
	}

	public Lookup getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(Lookup supplierName) {
		this.supplierName = supplierName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<AssetDTO> getChildAssets() {
		return childAssets;
	}

	public void setChildAssets(List<AssetDTO> childAssets) {
		this.childAssets = childAssets;
	}

	public List<ObservationDTO> getObservations() {
		return observations;
	}

	public void setObservations(List<ObservationDTO> observations) {
		this.observations = observations;
	}

	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

}
