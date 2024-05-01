package com.assetsense.tagbuilder.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class JsUtil {

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public native boolean isArray(JavaScriptObject obj) /*-{
		return Array.isArray(obj);
	}-*/;

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public native int getArrayLength(JavaScriptObject obj) /*-{
		return obj.length;
	}-*/;

	/**
	 * 
	 * @param obj
	 * @param index
	 * @return
	 */
	public native JavaScriptObject getArrayElement(JavaScriptObject obj, int index) /*-{
		return obj[index];
	}-*/;

	/**
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public native String getValueAsString(JavaScriptObject obj, String key) /*-{
		return obj[key];
	}-*/;

	/**
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public native JavaScriptObject getObjectProperty(JavaScriptObject obj, String key) /*-{
		return obj[key];
	}-*/;

	/**
	 * 
	 * @return
	 */
	public native String getAssetData() /*-{
		return $wnd.getAssetData();
	}-*/;

	public native String getResponseData() /*-{
		return $wnd.getResponseData();
	}-*/;

	public native void sendMessageToServer(String message, AsyncCallback<String> callback) /*-{
		$wnd.sendMessageToServer(
						message,
						function(data) {
							callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(data);
						});
	}-*/;
}
