package com.assetsense.tagbuilder.pi.domain;

import java.util.ArrayList;
import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Attribute;

public class Element {
	private String id;
	private String name;
	private List<Element> childElements = new ArrayList<>();
	private List<Attribute> attribute = new ArrayList<>();

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
