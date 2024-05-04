package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;

public class Lookup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String categoryId;
	private String name;
	
	
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
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	@Override
	public String toString() {
		return "Lookup [id=" + id + ", categoryId=" + categoryId + ", name=" + name + "]";
	}
	
	
	
}
