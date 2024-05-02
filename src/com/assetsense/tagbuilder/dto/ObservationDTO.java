package com.assetsense.tagbuilder.dto;

import java.io.Serializable;

import com.assetsense.tagbuilder.c2.domain.Lookup;

public class ObservationDTO implements Serializable {
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
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Lookup getCategory() {
		return category;
	}
	public void setCategory(Lookup category) {
		this.category = category;
	}
	
	
}
