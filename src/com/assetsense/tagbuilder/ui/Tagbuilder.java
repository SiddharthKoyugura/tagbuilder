package com.assetsense.tagbuilder.ui;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tagbuilder implements EntryPoint {

	private final TagBuilderPage tagBuilderPage = new TagBuilderPage();

	public void onModuleLoad() {
		tagBuilderPage.loadTagBuilderPage();

//		LookupServiceAsync lookupService = GWT.create(LookupService.class);

//		lookupService.getLookups(new AsyncCallback<List<Lookup>>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				Window.alert("onfailure");
//
//			}
//
//			@Override
//			public void onSuccess(List<Lookup> result) {
//				Window.alert(result.toString());
//
//			}
//
//		});

	}
}
