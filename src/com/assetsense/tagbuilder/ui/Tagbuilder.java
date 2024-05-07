package com.assetsense.tagbuilder.ui;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tagbuilder implements EntryPoint {

	private final TagBuilderPage tagBuilderPage = new TagBuilderPage();

	public void onModuleLoad() {
		tagBuilderPage.loadTagBuilderPage();

	}
}
