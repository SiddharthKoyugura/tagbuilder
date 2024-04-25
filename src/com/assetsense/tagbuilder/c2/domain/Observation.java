package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;

public class Observation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String code;
	private String formType;
	private String functionalCat;
	private String description;
	private Tag tag;

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
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getFunctionalCat() {
		return functionalCat;
	}

	public void setFunctionalCat(String functionalCat) {
		this.functionalCat = functionalCat;
	}

	public String getDescription() {
		return description;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
