package com.jnxxgc.answerclient.model;

public interface onDataResult {
	void onSuccess(String msg, AppendData appendData);

	void onFailed(String msg, String errorMsg);
}