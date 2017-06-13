package com.oppsis.app.hftracker.api.util;

import java.util.List;

import com.oppsis.app.hftracker.api.APIManager.ListCallback;

public class ListCallbackAdapter<T> implements ListCallback<T>{
	@Override
	public void success(List<T> result) {
	}

	@Override
	public void failure(Exception e) {
	}
	
}
