package com.assetsense.tagbuilder.ui;

import com.assetsense.tagbuilder.service.AssetService;
import com.assetsense.tagbuilder.service.AssetServiceAsync;
import com.assetsense.tagbuilder.service.LookupService;
import com.assetsense.tagbuilder.service.LookupServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import java.util.*;
import com.assetsense.tagbuilder.c2.domain.Lookup;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tagbuilder implements EntryPoint {

	private final TagBuilderPage tagBuilderPage = new TagBuilderPage();

	public void onModuleLoad() {
		tagBuilderPage.loadTagBuilderPage();
		
		LookupServiceAsync lookupService = GWT.create(LookupService.class);
		
		lookupService.getLookups(new AsyncCallback<List<Lookup>>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("onfailure");
				
			}

			@Override
			public void onSuccess(List<Lookup> result) {
			Window.alert(result.toString());
				
			}

			});
	

}
}
