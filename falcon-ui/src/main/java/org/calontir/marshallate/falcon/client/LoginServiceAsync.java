package org.calontir.marshallate.falcon.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	public void login(String loginTargetUri, String logoutTargetUri, AsyncCallback<LoginInfo> async);
}
