package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;

public class Observation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String code;
	private String inputType;
	private String description;
	private Lookup category;

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

	public String getFormType() {
		return inputType;
	}

	public void setFormType(String inputType) {
		this.inputType = inputType;
	}

	public String getDescription() {
		return description;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
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

}
