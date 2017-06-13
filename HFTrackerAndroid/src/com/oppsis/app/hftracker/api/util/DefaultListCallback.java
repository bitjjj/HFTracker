package com.oppsis.app.hftracker.api.util;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.api.APIManager.ListCallback;

public class DefaultListCallback<T> implements ListCallback<T> {

	private WeakReference<Context> mContext;
	private Handler mMainHandler;

	/**
	 * Make sure Toast run in main thread.
	 * @param activity
	 */
	public DefaultListCallback(Context context) {
		mContext = new WeakReference<Context>(context);
		mMainHandler = new Handler(context.getMainLooper());
	}

	@Override
	final public void success(List<T> result) {
		if(mContext.get() != null){
			onSuccess(result);
		}
	}
	
	public void onSuccess(List<T> result){
		
	}

	@Override
	public void failure(Exception e) {
		final Context context = mContext.get();
		if(context != null){
			if (e instanceof NetworkException) {
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
