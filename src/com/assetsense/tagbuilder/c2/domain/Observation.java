package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;

public class Observation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String code;
	private Lookup inputType;
	private String description;
	private Lookup category;
	private Lookup measurement;
	private Lookup unitid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public Lookup getCategory() {
		return category;
	}

	public void setCategory(Lookup category) {
		this.category = category;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Lookup getInputType() {
		return inputType;
	}

	public void setInputType(Lookup inputType) {
		this.inputType = inputType;
	}

	public Lookup getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Lookup measurement) {
		this.measurement = measurement;
	}

	public Lookup getUnitid() {
		return unitid;
	}

	public void setUnitid(Lookup unitid) {
		this.unitid = unitid;
	}

}
