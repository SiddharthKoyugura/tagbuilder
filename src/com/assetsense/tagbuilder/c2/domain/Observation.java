package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;

public class Observation extends PersistantObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String code;
	private Lookup inputType;
	private String description;
	private Lookup category;
	private Measurement measurement;
	private Lookup unitid;
	private Tag tag;
	private Double lowerLimit;
	private Double upperLimit;

	@Override
	public void detach() {
		super.detach();
		if (tag != null) {
			tag.detach();
		}
	}

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

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public Lookup getUnitid() {
		return unitid;
	}

	public void setUnitid(Lookup unitid) {
		this.unitid = unitid;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Double getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(Double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public Double getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(Double upperLimit) {
		this.upperLimit = upperLimit;
	}

	@Override
	public String toString() {
		return "Observation [id=" + id + ", code=" + code + ", inputType=" + inputType + ", description=" + description
				+ ", category=" + category + ", measurement=" + measurement + ", unitid=" + unitid + ", tag=" + tag
				+ "]";
	}

}
