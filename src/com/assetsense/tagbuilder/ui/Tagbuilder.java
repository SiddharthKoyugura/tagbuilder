package com.assetsense.tagbuilder.ui;

import java.util.ArrayList;
import java.util.List;

import com.assetsense.tagbuilder.c2.domain.Asset;
import com.assetsense.tagbuilder.c2.domain.Observation;
import com.assetsense.tagbuilder.c2.domain.Tag;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tagbuilder implements EntryPoint {
	
	private final TagBuilderPage tagBuilderPage = new TagBuilderPage();

	public void onModuleLoad() {
		
//		String data;
//		data = Interop.getData();
//		List<Asset> assets = new ArrayList<>();
//		if (data != null) {
//			JavaScriptObject jsArray = JsonUtils.safeEval(data);
//			for (int i = 0; i < getArrayLength(jsArray); i++) {
//				JavaScriptObject assetObject = getArrayElement(jsArray, i);
//				Asset asset = new Asset();
//				String name = getValueAsString(assetObject, "Name");
//				asset.setName(name);
//				String model = getValueAsString(assetObject, "Model");
//				asset.setModel(model);
//				String location = getValueAsString(assetObject, "Location");
//				asset.setLocation(location);
//				
//				String parentAsset = getValueAsString(assetObject, "ParentAsset");
//
//				JavaScriptObject tagsArray = getObjectProperty(assetObject, "Tags");
//				List<Tag> tags = new ArrayList<>();
//				if (tagsArray != null && isArray(tagsArray)) {
//					for (int j = 0; j < getArrayLength(tagsArray); j++) {
//						JavaScriptObject tagObject = getArrayElement(tagsArray, j);
//						Tag tag = new Tag();
//						String tagName = getValueAsString(tagObject, "Name");
//						String tagDescription = getValueAsString(tagObject, "Name");
//						tag.setName(tagName);
//						tag.setAsset(asset);
//						
//						
//						JavaScriptObject obsArray = getObjectProperty(assetObject, "Observations");
//						if(obsArray!=null && isArray(obsArray)){
//							for(int k=0;k<getArrayLength(obsArray);k++){
//								JavaScriptObject obsObject = getArrayElement(obsArray, k);
//								Observation observation = new Observation();
//								String code = getValueAsString(obsObject, "Code");
//								String type = getValueAsString(obsObject, "FormType");
//								String functionalCat = getValueAsString(obsObject, "FunctionalCat");
//								String description = getValueAsString(obsObject, "Description");
//								
//								observation.setCode(code);
//								observation.setFormType(type);
//								observation.setFunctionalCat(functionalCat);
//								observation.setDescription(description);
//							}
//						}
//						
//						
//						tags.add(tag);
//					}
//				}
//
//				GWT.log(asset.toString());
//			}
//		}
//		
//		DockLayoutPanel dpanel = new DockLayoutPanel(Unit.PX);
//		
//		
//		Tree tree = new Tree();
//		TreeItem item1 = new TreeItem(new Label("Hello"));
//		
//		item1.addItem(new TreeItem(new Label("Heyu")));
//		
//
//		tree.addItem(item1);
//		
//		
//		dpanel.addWest(tree, 200);
//		
//		HorizontalPanel hpanel = new HorizontalPanel();
//		hpanel.add(new Label("Hello"));
//		
//		dpanel.add(hpanel);
//		
//		RootLayoutPanel.get().add(dpanel);
		tagBuilderPage.loadTagBuilderPage();
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
