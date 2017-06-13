package com.oppsis.app.hftracker.api.util;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.APIManager.ObjectCallback;

public class DefaultObjectCallback<T> implements ObjectCallback<T>{
	
	private WeakReference<Context> mContext;
	private Handler mMainHandler;
	
	public DefaultObjectCallback(Activity context){
		mContext = new WeakReference<Context>(context);
		mMainHandler = new Handler(context.getMainLooper());
	}

	@Override
	final public void success(T result) {
		if(mContext.get() != null){
			onSuccess(result);
		}
	}
	
	public void onSuccess(T result){
		
	}

	@Override
	public void failure(Exception e) {
		final Context context = mContext.get();
		if(context != null){
			if(e instanceof NetworkException){
				mMainHandler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(context,context.getString(R.string.no_network_tip),Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
		else{
			onFailure(e);
		}
	}
	
	public void onFailure(Exception e){
		
	}

}
