package com.oppsis.app.hftracker.api.util;

import com.oppsis.app.hftracker.api.APIManager.ObjectCallback;


public class ObjectCallbackAdapter<T> implements ObjectCallback<T>{

	@Override
	public void success(T result) {
		
	}

	@Override
	public void failure(Exception e) {
		
	}

}
