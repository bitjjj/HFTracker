package com.oppsis.app.hftracker.app;

import java.util.HashSet;
import java.util.Set;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.oppsis.app.hftracker.R;
import com.oppsis.app.hftracker.util.Constants;
import com.oppsis.app.util.SharedPrefsUtils;

public class HFApplication extends Application{

	 @Override
     public void onCreate() {
         super.onCreate();
         //JPushInterface.setDebugMode(true);
         JPushInterface.init(this);
         
         boolean hasSet = SharedPrefsUtils.getBoolean(getApplicationContext(), Constants.JPUSH_TAG_STATUS, false);
         if(!hasSet){
        	 Set<String> tags = new HashSet<String>();
        	 tags.add(getApplicationContext().getString(R.string.language));
        	 JPushInterface.setTags(getApplicationContext(), tags, new TagAliasCallback(){

				@Override
				public void gotResult(int responseCode, String alias, Set<String> tags) {
					if(responseCode == 0){
						SharedPrefsUtils.save(getApplicationContext(), Constants.JPUSH_TAG_STATUS, true);
					}
				}
        		 
        	 });
         }
     }
}
