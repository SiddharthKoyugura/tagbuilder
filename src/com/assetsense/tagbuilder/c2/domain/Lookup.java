package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;

public class Lookup extends PersistantObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String categoryId;
	private String name;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
