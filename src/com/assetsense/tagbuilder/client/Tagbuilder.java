package com.assetsense.tagbuilder.client;

import java.util.ArrayList;
import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.c2.domain.Tag;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tagbuilder implements EntryPoint {

	public void onModuleLoad() {
		
		String data;
		data = Interop.getData();
		if (data != null) {
			JavaScriptObject jsArray = JsonUtils.safeEval(data);
			for (int i = 0; i < getArrayLength(jsArray); i++) {
				JavaScriptObject assetObject = getArrayElement(jsArray, i);
				Asset asset = new Asset();
				String name = getValueAsString(assetObject, "Name");
				String description = getValueAsString(assetObject, "Description");
				asset.setName(name);
				asset.setDescription(description);

				JavaScriptObject tagsArray = getObjectProperty(assetObject, "Tags");
				List<Tag> tags = new ArrayList<>();
				if (tagsArray != null && isArray(tagsArray)) {
					for (int j = 0; j < getArrayLength(tagsArray); j++) {
						JavaScriptObject tagObject = getArrayElement(tagsArray, j);
						Tag tag = new Tag();
						String tagName = getValueAsString(tagObject, "Name");
						String tagDescription = getValueAsString(tagObject, "Name");
						String type = getValueAsString(tagObject, "DataType");
						String engineeringUnits = getValueAsString(tagObject, "EngineeringUnits");
						tag.setName(tagName);
						tag.setDescription(tagDescription);
						tag.setDataType(type);
						tag.setEngineeringUnits(engineeringUnits);
						tags.add(tag);
					}
				}
				asset.setTags(tags);

				GWT.log(asset.toString());
			}
		}


	}

	private native boolean isArray(JavaScriptObject obj) /*-{
		return Array.isArray(obj);
	}-*/;

	private native int getArrayLength(JavaScriptObject obj) /*-{
		return obj.length;
	}-*/;

	private native JavaScriptObject getArrayElement(JavaScriptObject obj, int index) /*-{
		return obj[index];
	}-*/;

	private native String getValueAsString(JavaScriptObject obj, String key) /*-{
		return obj[key];
	}-*/;

	private native JavaScriptObject getObjectProperty(JavaScriptObject obj, String key) /*-{
		return obj[key];
	}-*/;

}
