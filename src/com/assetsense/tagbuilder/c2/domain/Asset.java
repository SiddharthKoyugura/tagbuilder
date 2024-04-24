package com.assetsense.tagbuilder.c2.domain;

import java.util.List;

public class Asset {
	private String name;
	private String description;
	private List<Tag> tags;

	public Asset() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Asset [name=" + name + ", description=" + description + ", tags=" + tags + "]";
	}

}
