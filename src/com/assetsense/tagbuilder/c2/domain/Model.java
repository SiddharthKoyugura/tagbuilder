package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;

public class Model extends PersistantObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private Lookup assetType;
	private Lookup supplierName;
	private Lookup assetCategory;

	@Override
	public void detach() {
		super.detach();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Lookup getAssetType() {
		return assetType;
	}

	public void setAssetType(Lookup assetType) {
		this.assetType = assetType;
	}

	public Lookup getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(Lookup supplierName) {
		this.supplierName = supplierName;
	}

	public Lookup getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(Lookup assetCategory) {
		this.assetCategory = assetCategory;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Model [name=" + name + ", assetType=" + assetType + ", supplierName=" + supplierName
				+ ", assetCategory=" + assetCategory + "]";
	}

}
