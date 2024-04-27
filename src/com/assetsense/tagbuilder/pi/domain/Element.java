package com.assetsense.tagbuilder.pi.domain;

import java.util.ArrayList;
import java.util.List;

public class Element {
	private String id;
	private String name;
	private List<Element> childElements = new ArrayList<>();;

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

	public List<Element> getChildElements() {
		return childElements;
	}

	public void setChildElements(List<Element> childElements) {
		this.childElements = childElements;
	}

	@Override
	public String toString() {
		return "Element [id=" + id + ", name=" + name + ", childElements=" + childElements + "]";
	}

}
