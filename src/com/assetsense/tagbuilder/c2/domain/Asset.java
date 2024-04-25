package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;

public class Asset implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String ecn;
	private String model;
	private String location;
	private Asset parentAsset;

	public Asset() {
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

	public String getEcn() {
		return ecn;
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

	public Asset getParentAsset() {
		return parentAsset;
	}

	public void setParentAsset(Asset parentAsset) {
		this.parentAsset = parentAsset;
	}

}
