package com.assetsense.tagbuilder.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tagbuilder implements EntryPoint {

	private final TagBuilderPage tagBuilderPage = new TagBuilderPage();

	public void onModuleLoad() {
		Timer timer = new Timer() {
			@Override
			public void run() {
				tagBuilderPage.loadTagBuilderPage();
			}
		};
		timer.schedule(1000);
	}
}
