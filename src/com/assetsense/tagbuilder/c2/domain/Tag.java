package com.assetsense.tagbuilder.c2.domain;

public class Tag {
	private String name;
	private String description;
	private String dataType;
	private String engineeringUnits;

	public Tag() {
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

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getEngineeringUnits() {
		return engineeringUnits;
	}

	public void setEngineeringUnits(String engineeringUnits) {
		this.engineeringUnits = engineeringUnits;
	}

	@Override
	public String toString() {
		return "Tag [name=" + name + ", description=" + description + ", dataType=" + dataType + ", engineeringUnits="
				+ engineeringUnits + "]";
	}

}
