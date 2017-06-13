package com.oppsis.app.hftracker.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iainconnor.objectcache.CacheManager;
import com.iainconnor.objectcache.DiskCache;
import com.iainconnor.objectcache.ex.GetCallbackAdapter;
import com.iainconnor.objectcache.ex.PutCallbackAdapter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.ion.Ion;
import com.oppsis.app.hftracker.api.util.ListParameterizedType;
import com.oppsis.app.hftracker.api.util.NetworkException;
import com.oppsis.app.hftracker.api.util.ObjectParameterizedType;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.util.FuncUtils;
import com.oppsis.app.util.NetworkUtils;
import com.oppsis.app.util.UrlUtils;

public class APIManager {

	private final static int CACHE_SIZE = 1024 * 1024  * 50;//50M
	private final static int TIMEOUT = 5 * 60 * 60 * 1000;//5 minutes
	private static APIManager mInstance;
	private Context mContext;
	private CacheManager mCacheManager;
	
	public static APIManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new APIManager(context);
		}

		return mInstance;
	}

	private APIManager(Context context) {
		this.mContext = context;

		String cachePath = context.getCacheDir().getPath();
		File cacheFile = new File(cachePath + File.separator + context.getPackageName());

		DiskCache diskCache = null;
		try {
			diskCache = new DiskCache(cacheFile, context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode, CACHE_SIZE);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mCacheManager = CacheManager.getInstance(diskCache);
	}
	
	public static interface FailureCallback{
		void failure(Exception e);
	}
	
	public static interface ListCallback<T> extends FailureCallback{
		void success(List<T> result);
	}
	
	public static interface ObjectCallback<T> extends FailureCallback{
		void success(T result);
	}
	
	/**
	 * Caller need be in background thread
	 * @param url
	 * @param clazz
	 * @param callback Attention: When updating UI window , it need post to UI thread
	 */
	public <T> void listSync(final String url,final Class<T> clazz,final ListCallback<T> callback){
		list(url,clazz,callback,true);
	}
	
	/**
	 * Caller can be in UI thread
	 * @param url
	 * @param clazz
	 * @param callback Attention: Can update UI window in callback directly
	 */
	public <T> void listAsync(final String url,final Class<T> clazz,final ListCallback<T> callback){
		list(url,clazz,callback,false);
	}
	
	@SuppressWarnings("unchecked")
	private <T> void list(final String url,final Class<T> clazz,final ListCallback<T> callback,boolean isSync){
		Map<String, String> urlParameters = UrlUtils.getParameters(url);
		String ifCache = urlParameters.get(Constants.URL_KEY_CACHE),
				expiryTime = urlParameters.get(Constants.URL_KEY_EXPIRY),
				ifRefresh = urlParameters.get(Constants.URL_KEY_REFRESH);
		final String cacheKey = FuncUtils.md5(url);
		final int expirySeconds;
		if(expiryTime != null){
			expirySeconds = Integer.valueOf(expiryTime);
		}
		else {
			expirySeconds = CacheManager.ExpiryTimes.ONE_DAY.asSeconds();
		}
		
		if(ifCache != null && ifCache.equals(Constants.FALSE_STR)){//no cache
			if(isSync){
				httpWithAA(url, new HttpAACompletedAdapter(callback) {
					@Override
					public void complete(JSONObject result) {
						List<T> resultList = getListResults(result,clazz);
		        	    callback.success(resultList);
					}
				});
			}
			else{
				httpWithIon(url, new HttpIonCompletedAdapter(callback) {				
					@Override
					public void complete(JsonObject result) {
						List<T> resultList = getListResults(result,clazz);
		        	    callback.success(resultList);
					}
				});
			}
		}
		else if(ifRefresh != null && ifRefresh.equals(Constants.TRUE_STR)){//refresh cache forcibly
			
			if(isSync){
				cacheListSyncWithAA(url, expirySeconds, clazz, callback);
			}
			else{
				cacheListAsyncWithIon(url, expirySeconds, clazz, callback);
			}
		}
		else{//cache
			if(isSync){
				List<T> dataList = (List<T>)mCacheManager.get(cacheKey, List.class, new ListParameterizedType<T>(clazz));
				if (dataList != null ) {
		        	callback.success(dataList);
		        } 
		        else {
		        	cacheListSyncWithAA(url, expirySeconds, clazz, callback);
		        }
			}
			else{
				mCacheManager.getAsync(cacheKey, List.class, new ListParameterizedType<T>(clazz), new GetCallbackAdapter<List<T>>() {
				    @Override
				    public void onSuccess (List<T> dataList) {
				        if (dataList != null ) {
				        	callback.success(dataList);
				        } 
				        else {
				        	cacheListAsyncWithIon(url, expirySeconds, clazz, callback);
				        }
				    }
				});
			}
		}
	}
	
	public <T> void objectSync(final String url,final Class<T> clazz,final ObjectCallback<T> callback){
		object(url,clazz,callback,true);
	}
	
	
	public <T> void objectAsync(final String url,final Class<T> clazz,final ObjectCallback<T> callback){
		object(url,clazz,callback,false);
	}
	
	@SuppressWarnings("unchecked")
	private <T> void object(final String url,final Class<T> clazz,final ObjectCallback<T> callback,boolean isSync){
		Map<String, String> urlParameters = UrlUtils.getParameters(url);
		String ifCache = urlParameters.get(Constants.URL_KEY_CACHE),
				expiryTime = urlParameters.get(Constants.URL_KEY_EXPIRY),
				ifRefresh = urlParameters.get(Constants.URL_KEY_REFRESH);
		final String cacheKey = FuncUtils.md5(url);
		final int expirySeconds;
		if(expiryTime != null){
			expirySeconds = Integer.valueOf(expiryTime);
		}
		else {
			expirySeconds = CacheManager.ExpiryTimes.ONE_DAY.asSeconds();
		}
		
		if(ifCache != null && ifCache.equals(Constants.FALSE_STR)){//no cache
			if(isSync){
				httpWithAA(url, new HttpAACompletedAdapter(callback) {				
					@Override
					public void complete(JSONObject result) {
						T obj = getObjectResult(result,clazz);
						callback.success(obj);
					}
				});
			}
			else{
				httpWithIon(url, new HttpIonCompletedAdapter(callback) {				
					@Override
					public void complete(JsonObject result) {
						T obj = getObjectResult(result,clazz);
						callback.success(obj);
					}
				});
			}
		}
		else if(ifRefresh != null && ifRefresh.equals(Constants.TRUE_STR)){//refresh cache forcibly
			if(isSync){
				cacheObjectSyncWithAA(url, expirySeconds, clazz, callback);
			}
			else{
				cacheObjectAsyncWithIon(url, expirySeconds, clazz, callback);
			}
		}
		else{//cache
			if(isSync){
				T data = (T)mCacheManager.get(cacheKey, clazz, new ObjectParameterizedType<T>(clazz));
				if (data != null ) {
		        	callback.success(data);
		        } else {
		        	cacheObjectSyncWithAA(url, expirySeconds, clazz, callback);
		        }
			}
			else{
				mCacheManager.getAsync(cacheKey, clazz, new ObjectParameterizedType<T>(clazz), new GetCallbackAdapter<T>() {
				    @Override
				    public void onSuccess (T data) {
				        if (data != null ) {
				        	callback.success(data);
				        } else {
				        	cacheObjectAsyncWithIon(url, expirySeconds, clazz, callback);
				        }
				    }
				});
			}
		}
	}
	
	private <T> void cacheListAsyncWithIon(String url,final int expirySeconds, final Class<T> clazz,final ListCallback<T> callback){
		final String cacheKey = FuncUtils.md5(url);
		httpWithIon(url, new HttpIonCompletedAdapter(callback) {				
			@Override
			public void complete(JsonObject result) {
				List<T> resultList = getListResults(result,clazz);
				if(resultList != null && resultList.size() != 0){//don't cache null or empty list
					mCacheManager.putAsync(cacheKey, resultList, expirySeconds, false,new PutCallbackAdapter() {});
				}
        	    callback.success(resultList);
			}
		});
	}
	
	private <T> void cacheObjectAsyncWithIon(String url,final int expirySeconds, final Class<T> clazz,final ObjectCallback<T> callback){
		final String cacheKey = FuncUtils.md5(url);
		httpWithIon(url, new HttpIonCompletedAdapter(callback) {				
			@Override
			public void complete(JsonObject result) {
				T obj = getObjectResult(result,clazz);
				mCacheManager.putAsync(cacheKey, obj, expirySeconds, false,new PutCallbackAdapter() {});
				callback.success(obj);
			}
		});
	}
	
	private <T> void cacheListSyncWithAA(String url,final int expirySeconds, final Class<T> clazz,final ListCallback<T> callback){
		final String cacheKey = FuncUtils.md5(url);
		httpWithAA(url, new HttpAACompletedAdapter(callback) {				
			@Override
			public void complete(JSONObject result) {
				List<T> resultList = getListResults(result,clazz);
				if(resultList != null && resultList.size() != 0){
					mCacheManager.put(cacheKey, resultList, expirySeconds, false);
				}
        	    callback.success(resultList);
			}
		});
	}
	
	private <T> void cacheObjectSyncWithAA(String url,final int expirySeconds, final Class<T> clazz,final ObjectCallback<T> callback){
		final String cacheKey = FuncUtils.md5(url);
		httpWithAA(url, new HttpAACompletedAdapter(callback) {				
			@Override
			public void complete(JSONObject result) {
				T obj = getObjectResult(result,clazz);
				mCacheManager.put(cacheKey, obj, expirySeconds, false);
				callback.success(obj);
			}
		});
	}
	
	private void httpWithIon(String url,final HttpIonCompleted completed){
		if(!NetworkUtils.isConnect(mContext)){
			completed.error(new NetworkException());
		}
		else{
			Ion.with(mContext)
	    	.load(url)
	    	.setTimeout(TIMEOUT)
	    	.asJsonObject()
	    	.setCallback(new FutureCallback<JsonObject>() {
	    	   @Override
	    	    public void onCompleted(Exception e, JsonObject result) {
	    		   if (e != null) {
	    	           completed.error(e);
	    	       }
	    		   else{
	    			   completed.complete(result);
	    		   }
	    	    }
	    	});
		}
	}
	

	private void httpWithAA(String url,final HttpAACompleted completed){
		if(!NetworkUtils.isConnect(mContext)){
			completed.error(new NetworkException());
		}
		else{
			AsyncHttpGet getMethod = new AsyncHttpGet(url);
			getMethod.setTimeout(TIMEOUT);
			AsyncHttpClient.getDefaultInstance().executeJSONObject(getMethod, new AsyncHttpClient.JSONObjectCallback() {
			    @Override
			    public void onCompleted(Exception e, AsyncHttpResponse response, JSONObject result) {
			    	if (e != null) {
			    		completed.error(e);
		    	    }
		    		else{
		    			completed.complete(result);
		    		}
			    }
			});
		}
	}
	
	/**
	 * @param result
	 * @param clazz
	 * @return
	 */
	private <T> List<T> getListResults(JsonObject result,Class<T> clazz){
		if(result == null || !result.has("results")){
			return new ArrayList<T>();//TODO:[Next Release]give empty or error tip
		}
		else{
			return new Gson().fromJson(result.getAsJsonArray("results").toString(), new ListParameterizedType<T>(clazz));
		}
	}
	
	private <T> T getObjectResult(JsonObject result,Class<T> clazz){
		if(result == null || !result.has("result")){
			return null;
		}
		else{
			return new Gson().fromJson(result.getAsJsonObject("result").toString(), clazz);
		}
	}
	
	private <T> List<T> getListResults(JSONObject result,Class<T> clazz){
		if(result == null || !result.has("results")){
			return new ArrayList<T>();
		}
		else{
			return new Gson().fromJson(result.optJSONArray("results").toString(), new ListParameterizedType<T>(clazz));
		}
	}
	
	private <T> T getObjectResult(JSONObject result,Class<T> clazz){
		if(result == null || !result.has("result")){
			return null;
		}
		else{
			return new Gson().fromJson(result.optJSONObject("result").toString(), clazz);
		}
	}
	
	private static interface HttpIonCompleted{
		void complete(JsonObject result);
		void error(Exception e);
	}
	
	private static interface HttpAACompleted{
		void complete(JSONObject result);
		void error(Exception e);
	}
	
	private static class HttpIonCompletedAdapter implements HttpIonCompleted{
		FailureCallback mErrorCB;
		
		public HttpIonCompletedAdapter(FailureCallback errorCB){
			mErrorCB = errorCB;
		}

		@Override
		public void complete(JsonObject result) {
		}

		@Override
		public void error(Exception e) {
			mErrorCB.failure(e);
		}
	}
	
	private static class HttpAACompletedAdapter implements HttpAACompleted{
		FailureCallback mErrorCB;
		
		public HttpAACompletedAdapter(FailureCallback errorCB){
			mErrorCB = errorCB;
		}

		@Override
		public void complete(JSONObject result) {
		}

		@Override
		public void error(Exception e) {
			mErrorCB.failure(e);
		}
	}
	
	
}
