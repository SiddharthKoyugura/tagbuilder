package com.assetsense.tagbuilder.c2.domain;

import java.util.ArrayList;
import java.util.List;

public class Element {
	private int id;
	private String elementName;
	private String elementTemplate;
	private List<Element> element = new ArrayList<>();
	private List<Attribute> attribute = new ArrayList<>();

	public String getElementTemplate() {
		return elementTemplate;
	}

	public void setElementTemplate(String elementTemplate) {
		this.elementTemplate = elementTemplate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Element> getElement() {
		return element;
	}

	public void setElement(List<Element> element) {
		this.element = element;
	}

	public List<Attribute> getAttribute() {
		return attribute;
	}

	public void setAttribute(List<Attribute> attribute) {
		this.attribute = attribute;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	

}
