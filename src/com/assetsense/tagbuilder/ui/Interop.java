package com.assetsense.tagbuilder.ui;

import com.google.gwt.core.client.JavaScriptObject;

public class Interop extends JavaScriptObject {
	protected Interop() {
	}

	public static String getData() {
		return getAssetData();
	}

	private native static String getAssetData() /*-{
		return $wnd.getAssetData();
	}-*/;
}
